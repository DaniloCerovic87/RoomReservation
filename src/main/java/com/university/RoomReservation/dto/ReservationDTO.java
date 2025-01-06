package com.university.RoomReservation.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDTO {

    private Long id;

    private Long userId;

    private Long roomId;

    private String employeeFullName;

    private String roomName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String reservationPurpose;

    private String reservationStatus;

    private String subject;

    private Integer semester;

    private String classType;

    private String examType;

    private String meetingName;

    private String meetingDescription;

    private String eventName;

    private String eventDescription;

}
