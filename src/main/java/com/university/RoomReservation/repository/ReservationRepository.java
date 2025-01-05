package com.university.RoomReservation.repository;

import com.university.RoomReservation.model.Reservation;
import com.university.RoomReservation.model.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Collection<Reservation> findByRoomId(Long roomId);

    boolean existsByRoomId(Long id);

    @Query("SELECT r FROM Reservation r " +
            "WHERE (:type IS NULL OR TYPE(r) = :type) " +
            "AND (:startTime IS NULL OR r.startTime >= :startTime) " +
            "AND (:endTime IS NULL OR r.endTime <= :endTime) " +
            "AND (:status IS NULL OR r.reservationStatus = :status) " +
            "AND (:roomName IS NULL OR :roomName = '' OR r.room.name LIKE %:roomName%)")
    Page<Reservation> findReservationsWithFilters(
            @Param("type") Class<?> type,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") ReservationStatus status,
            @Param("roomName") String roomName,
            Pageable pageable);

}
