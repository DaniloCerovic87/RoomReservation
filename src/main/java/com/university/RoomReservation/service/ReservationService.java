package com.university.RoomReservation.service;

import com.university.RoomReservation.request.CreateReservationRequest;
import com.university.RoomReservation.response.ReservationResponse;

public interface ReservationService {

    ReservationResponse createReservation(CreateReservationRequest request);

}
