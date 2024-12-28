package com.university.RoomReservation.service.impl;

import com.university.RoomReservation.dto.RoomDTO;
import com.university.RoomReservation.exception.ResourceNotFoundException;
import com.university.RoomReservation.mapper.RoomMapper;
import com.university.RoomReservation.model.Room;
import com.university.RoomReservation.repository.RoomRepository;
import com.university.RoomReservation.request.CreateRoomRequest;
import com.university.RoomReservation.request.UpdateRoomRequest;
import com.university.RoomReservation.service.RoomService;
import com.university.RoomReservation.util.RoomValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.university.RoomReservation.constants.MessageProperties.ROOM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(RoomMapper::toRoomResponse)
                .toList();
    }

    @Override
    public RoomDTO getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));
        return RoomMapper.toRoomResponse(room);
    }

    @Override
    public RoomDTO createRoom(CreateRoomRequest request) {
        RoomValidator.validateRoom(request.getRoomType(), request.getNumberOfComputers());
        Room room = RoomMapper.toEntity(request);
        return RoomMapper.toRoomResponse(roomRepository.save(room));
    }

    @Override
    public RoomDTO updateRoom(Long id, UpdateRoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));
        RoomValidator.validateRoom(request.getRoomType(), request.getNumberOfComputers());
        RoomMapper.updateRoomFromRequest(request, room);
        return RoomMapper.toRoomResponse(roomRepository.save(room));
    }

    @Override
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException(ROOM_NOT_FOUND);
        }
        //TODO - ask professor what is the best way to fetch reservations
        roomRepository.deleteById(id);
    }

}
