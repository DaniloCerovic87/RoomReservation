package com.university.RoomReservation.repository;

import com.university.RoomReservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Collection<Reservation> findByRoomId(Long roomId);

}
