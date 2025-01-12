package com.university.room.reservation.service.impl;

import com.university.room.reservation.dto.RoomDTO;
import com.university.room.reservation.exception.ResourceNotFoundException;
import com.university.room.reservation.exception.ValidationException;
import com.university.room.reservation.mapper.RoomMapper;
import com.university.room.reservation.model.Room;
import com.university.room.reservation.repository.ReservationRepository;
import com.university.room.reservation.repository.RoomRepository;
import com.university.room.reservation.request.RoomRequest;
import com.university.room.reservation.service.RoomService;
import com.university.room.reservation.util.RoomValidator;
import com.university.room.reservation.constants.MessageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .orElseThrow(() -> new ResourceNotFoundException(MessageProperties.ROOM_NOT_FOUND));
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
                .orElseThrow(() -> new ResourceNotFoundException(MessageProperties.ROOM_NOT_FOUND));
        RoomValidator.validateRoom(request.getRoomType(), request.getNumberOfComputers());
        RoomMapper.updateRoomFromRequest(request, room);
        return RoomMapper.toDto(roomRepository.save(room));
    }

    @Override
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException(MessageProperties.ROOM_NOT_FOUND);
        }

        if (reservationRepository.existsByRoomId(id)) {
            throw new ValidationException(MessageProperties.RESERVATION_EXISTS_FOR_ROOM);
        }

        roomRepository.deleteById(id);
    }

}
