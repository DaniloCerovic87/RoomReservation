package com.university.RoomReservation.service;

import com.university.RoomReservation.request.CreateRoomRequest;
import com.university.RoomReservation.request.UpdateRoomRequest;
import com.university.RoomReservation.response.RoomResponse;

import java.util.List;

public interface RoomService {

    List<RoomResponse> getAllRooms();

    RoomResponse getRoomById(Long id);

    RoomResponse createRoom(CreateRoomRequest request);

    RoomResponse updateRoom(Long id, UpdateRoomRequest request);

    void deleteRoom(Long id);


}