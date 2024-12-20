package com.university.RoomReservation.service.impl;

import com.university.RoomReservation.exception.ResourceNotFoundException;
import com.university.RoomReservation.exception.ValidationException;
import com.university.RoomReservation.mapper.RoomMapper;
import com.university.RoomReservation.model.Room;
import com.university.RoomReservation.model.enums.RoomType;
import com.university.RoomReservation.repository.RoomRepository;
import com.university.RoomReservation.request.CreateRoomRequest;
import com.university.RoomReservation.request.UpdateRoomRequest;
import com.university.RoomReservation.response.RoomResponse;
import com.university.RoomReservation.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.university.RoomReservation.constants.MessageProperties.*;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(RoomMapper::toRoomResponse)
                .toList();
    }

    @Override
    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));
        return RoomMapper.toRoomResponse(room);
    }

    @Override
    public RoomResponse createRoom(CreateRoomRequest request) {
        validateRoom(request.getRoomType(), request.getNumberOfComputers());
        Room room = RoomMapper.toRoom(request);
        return RoomMapper.toRoomResponse(roomRepository.save(room));
    }

    @Override
    public RoomResponse updateRoom(Long id, UpdateRoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));
        validateRoom(request.getRoomType(), request.getNumberOfComputers());
        RoomMapper.updateRoomFromRequest(request, room);
        return RoomMapper.toRoomResponse(roomRepository.save(room));
    }

    @Override
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException(ROOM_NOT_FOUND);
        }
        // maybe delete reservations if not any in progress or reserved
        roomRepository.deleteById(id);
    }

    private void validateRoom(String roomType, Integer numberOfComputers) {
        if (roomType.equals(RoomType.COMPUTER_ROOM.getValue()) &&
                (numberOfComputers == null || numberOfComputers <= 0)) {
            throw new ValidationException(COMPUTER_INVALID_COUNT);
        }

        if ((roomType.equals(RoomType.CLASSROOM.getValue()) ||
                roomType.equalsIgnoreCase(RoomType.AMPHITHEATER.getValue())) && numberOfComputers != null) {
            throw new ValidationException(COMPUTERS_NOT_ALLOWED);
        }
    }

}
