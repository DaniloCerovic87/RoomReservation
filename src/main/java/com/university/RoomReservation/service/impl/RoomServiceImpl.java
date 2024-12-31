package com.university.RoomReservation.service.impl;

import com.university.RoomReservation.dto.RoomDTO;
import com.university.RoomReservation.exception.ResourceNotFoundException;
import com.university.RoomReservation.exception.ValidationException;
import com.university.RoomReservation.mapper.RoomMapper;
import com.university.RoomReservation.model.Room;
import com.university.RoomReservation.repository.ReservationRepository;
import com.university.RoomReservation.repository.RoomRepository;
import com.university.RoomReservation.request.RoomRequest;
import com.university.RoomReservation.service.RoomService;
import com.university.RoomReservation.util.RoomValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.university.RoomReservation.constants.MessageProperties.RESERVATION_EXISTS_FOR_ROOM;
import static com.university.RoomReservation.constants.MessageProperties.ROOM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    private final ReservationRepository reservationRepository;

    @Override
    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(RoomMapper::toDto)
                .toList();
    }

    @Override
    public RoomDTO getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));
        return RoomMapper.toDto(room);
    }

    @Override
    public RoomDTO createRoom(RoomRequest request) {
        RoomValidator.validateRoom(request.getRoomType(), request.getNumberOfComputers());
        Room room = RoomMapper.toEntity(request);
        return RoomMapper.toDto(roomRepository.save(room));
    }

    @Override
    public RoomDTO updateRoom(Long id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));
        RoomValidator.validateRoom(request.getRoomType(), request.getNumberOfComputers());
        RoomMapper.updateRoomFromRequest(request, room);
        return RoomMapper.toDto(roomRepository.save(room));
    }

    @Override
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException(ROOM_NOT_FOUND);
        }

        if (reservationRepository.existsByRoomId(id)) {
            throw new ValidationException(RESERVATION_EXISTS_FOR_ROOM);
        }

        roomRepository.deleteById(id);
    }

}
