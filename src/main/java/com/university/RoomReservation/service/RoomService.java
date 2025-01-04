package com.university.RoomReservation.service;

import com.university.RoomReservation.dto.RoomDTO;
import com.university.RoomReservation.request.RoomRequest;

import java.util.List;

public interface RoomService {

    List<RoomDTO> getAllRooms();

    RoomDTO getRoomById(Long id);

    RoomDTO createRoom(RoomRequest request);

    RoomDTO updateRoom(Long id, RoomRequest request);

    void deleteRoom(Long id);

}