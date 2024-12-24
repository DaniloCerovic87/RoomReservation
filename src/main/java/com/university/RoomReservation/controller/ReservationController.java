package com.university.RoomReservation.controller;

import com.university.RoomReservation.dto.ReservationDTO;
import com.university.RoomReservation.request.CreateReservationRequest;
import com.university.RoomReservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@Valid @RequestBody CreateReservationRequest request) {
        ReservationDTO createdReservation = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }
}