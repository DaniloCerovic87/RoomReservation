package com.university.RoomReservation.service;

import com.university.RoomReservation.dto.RoomDTO;
import com.university.RoomReservation.request.CreateRoomRequest;
import com.university.RoomReservation.request.UpdateRoomRequest;

import java.util.List;

public interface RoomService {

    List<RoomDTO> getAllRooms();

    RoomDTO getRoomById(Long id);

    RoomDTO createRoom(CreateRoomRequest request);

    RoomDTO updateRoom(Long id, UpdateRoomRequest request);

    void deleteRoom(Long id);


}