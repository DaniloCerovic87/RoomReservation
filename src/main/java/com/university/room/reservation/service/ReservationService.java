package com.university.room.reservation.service;

import com.university.room.reservation.dto.ReservationDTO;
import com.university.room.reservation.request.ReservationFilterFormRequest;
import com.university.room.reservation.request.ReservationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservationService {

    List<ReservationDTO> getReservationsByRoom(Long roomId);

    ReservationDTO createReservation(ReservationRequest request);

    void deleteReservation(Long id);

    ReservationDTO getReservationById(Long id);

    ReservationDTO updateReservation(Long id, ReservationRequest request);

    Page<ReservationDTO> findReservations(ReservationFilterFormRequest filterForm, Pageable pageable);
}
