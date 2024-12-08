package com.university.RoomReservation.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("Meeting")
@Data
@EqualsAndHashCode(callSuper = true)
public class MeetingReservation extends Reservation {

    @NotBlank
    private String meetingName;

    @NotBlank
    private String meetingDescription;

}

