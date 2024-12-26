package com.university.RoomReservation.service;

import com.university.RoomReservation.dto.ReservationDTO;
import com.university.RoomReservation.request.CreateReservationRequest;

import java.util.List;

public interface ReservationService {

    List<ReservationDTO> getReservationsByRoom(Long roomId);

    ReservationDTO createReservation(CreateReservationRequest request);

}
