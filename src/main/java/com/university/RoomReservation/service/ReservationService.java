package com.university.RoomReservation.service;

import com.university.RoomReservation.request.ReservationRequest;
import com.university.RoomReservation.response.ReservationResponse;

public interface ReservationService {

    ReservationResponse createReservation(ReservationRequest request);

}
