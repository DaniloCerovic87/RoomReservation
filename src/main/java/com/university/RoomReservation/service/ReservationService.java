package com.university.RoomReservation.service;

import com.university.RoomReservation.dto.ReservationDTO;
import com.university.RoomReservation.request.ReservationFilterFormRequest;
import com.university.RoomReservation.request.ReservationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservationService {

    List<ReservationDTO> getReservationsByRoom(Long roomId);

    ReservationDTO createReservation(ReservationRequest request);

    void deleteReservation(Long id);

    ReservationDTO getReservationById(Long id);

    Page<ReservationDTO> findReservations(ReservationFilterFormRequest filterForm, Pageable pageable);
}
