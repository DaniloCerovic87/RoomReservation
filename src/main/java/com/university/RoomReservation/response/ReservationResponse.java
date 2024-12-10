package com.university.RoomReservation.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponse {

    private Long id;

    private Long userId;

    private Long roomId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String reservationPurpose;

    private String subject;

    private Integer semester;

    private String classType;

    private String examType;

    private String meetingName;

    private String meetingDescription;

    private String eventName;

    private String eventDescription;

}
