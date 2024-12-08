package com.university.RoomReservation.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("Event")
@Data
@EqualsAndHashCode(callSuper = true)
public class EventReservation extends Reservation {

    @NotBlank
    private String eventName;

    @NotBlank
    private String eventDescription;

}