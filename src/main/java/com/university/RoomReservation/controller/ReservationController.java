package com.university.RoomReservation.controller;

import com.university.RoomReservation.dto.ReservationDTO;
import com.university.RoomReservation.request.ReservationFilterFormRequest;
import com.university.RoomReservation.request.ReservationRequest;
import com.university.RoomReservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<Page<ReservationDTO>> findReservations(ReservationFilterFormRequest filterForm, Pageable pageable) {
        Page<ReservationDTO> page = reservationService.findReservations(filterForm, pageable);
        return new ResponseEntity<>(page, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@Valid @RequestBody ReservationRequest request) {
        ReservationDTO createdReservation = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable Long id, @Valid @RequestBody ReservationRequest request) {
        ReservationDTO updatedReservation = reservationService.updateReservation(id, request);
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<List<ReservationDTO>> getReservationByRoom(@PathVariable("id") Long roomId) {
        return ResponseEntity.ok(reservationService.getReservationsByRoom(roomId));
    }

}