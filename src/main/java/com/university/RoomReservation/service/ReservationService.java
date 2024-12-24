package com.university.RoomReservation.service;

import com.university.RoomReservation.dto.ReservationDTO;
import com.university.RoomReservation.request.CreateReservationRequest;

public interface ReservationService {

    ReservationDTO createReservation(CreateReservationRequest request);

}
