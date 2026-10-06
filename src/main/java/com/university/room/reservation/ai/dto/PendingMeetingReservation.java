package com.university.room.reservation.ai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PendingMeetingReservation {

    private String conversationId;

    private Long roomId;

    private String startTime;

    private String endTime;

    private String reservationPurpose;

    private String meetingName;

    private String meetingDescription;

}
