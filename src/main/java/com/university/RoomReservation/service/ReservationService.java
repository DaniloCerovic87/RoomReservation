package com.university.RoomReservation.service;

import com.university.RoomReservation.dto.ReservationDTO;
import com.university.RoomReservation.request.ReservationRequest;

import java.util.List;

public interface ReservationService {

    List<ReservationDTO> getReservationsByRoom(Long roomId);

    ReservationDTO createReservation(ReservationRequest request);

    void deleteReservation(Long id);

    ReservationDTO getReservationById(Long id);

}
