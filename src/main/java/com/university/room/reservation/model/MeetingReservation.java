package com.university.room.reservation.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("MEETING")
@Data
@EqualsAndHashCode(callSuper = true)
public class MeetingReservation extends Reservation {

    private String meetingName;

    private String meetingDescription;

}

