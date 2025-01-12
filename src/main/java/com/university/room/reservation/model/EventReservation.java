package com.university.room.reservation.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("EVENT")
@Data
@EqualsAndHashCode(callSuper = true)
public class EventReservation extends Reservation {

    private String eventName;

    private String eventDescription;

}