package com.university.room.reservation.service;

import com.university.room.reservation.dto.RoomDTO;
import com.university.room.reservation.request.RoomRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RoomService {

    List<RoomDTO> getAllRooms();

    List<RoomDTO> findAvailableRooms(LocalDate date, LocalTime startTime, LocalTime endTime, Integer capacity);

    RoomDTO getRoomById(Long id);

    RoomDTO createRoom(RoomRequest request);

    RoomDTO updateRoom(Long id, RoomRequest request);

    void deleteRoom(Long id);

}
