package com.university.room.reservation.service;

import com.university.room.reservation.dto.RoomDTO;
import com.university.room.reservation.request.RoomRequest;

import java.util.List;

public interface RoomService {

    List<RoomDTO> getAllRooms();

    RoomDTO getRoomById(Long id);

    RoomDTO createRoom(RoomRequest request);

    RoomDTO updateRoom(Long id, RoomRequest request);

    void deleteRoom(Long id);

}