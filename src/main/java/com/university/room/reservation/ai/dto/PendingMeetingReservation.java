package com.university.room.reservation.ai.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PendingMeetingReservation {

    private String conversationId;

    private Long userId;

    private Long roomId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String reservationPurpose;

    private String meetingName;

    private String meetingDescription;

}
