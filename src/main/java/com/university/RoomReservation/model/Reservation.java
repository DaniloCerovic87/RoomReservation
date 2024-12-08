package com.university.RoomReservation.model;

import com.university.RoomReservation.model.enums.ReservationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "reservation_purpose", discriminatorType = DiscriminatorType.STRING)
@Data
public abstract class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @NotNull
    private Room room;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ReservationStatus reservationStatus;

    @Transient
    public String getReservationPurpose() {
        return this.getClass().getAnnotation(DiscriminatorValue.class).value();
    }

}