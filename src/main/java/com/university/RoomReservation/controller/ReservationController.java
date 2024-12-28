package com.university.RoomReservation.controller;

import com.university.RoomReservation.dto.ReservationDTO;
import com.university.RoomReservation.dto.RoomDTO;
import com.university.RoomReservation.request.CreateReservationRequest;
import com.university.RoomReservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@Valid @RequestBody CreateReservationRequest request) {
        ReservationDTO createdReservation = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}