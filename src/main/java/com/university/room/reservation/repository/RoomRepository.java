package com.university.room.reservation.repository;


import com.university.room.reservation.model.Room;
import com.university.room.reservation.model.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("""
            SELECT room FROM Room room
            WHERE room.capacity >= :capacity
            AND NOT EXISTS (
                SELECT reservation.id FROM Reservation reservation
                WHERE reservation.room = room
                AND reservation.reservationStatus IN :blockingStatuses
                AND reservation.startTime < :endTime
                AND reservation.endTime > :startTime
            )
            """)
    List<Room> findAvailableRooms(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("capacity") Integer capacity,
            @Param("blockingStatuses") Collection<ReservationStatus> blockingStatuses);
}
