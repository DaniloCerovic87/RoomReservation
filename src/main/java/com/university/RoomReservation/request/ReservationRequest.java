package com.university.RoomReservation.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Room ID is required")
    private Long roomId;

    @NotNull(message = "Start time is required")
    @FutureOrPresent(message = "Start time must be in the future or present")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @FutureOrPresent(message = "End time must be in the future or present")
    private LocalDateTime endTime;

    @NotBlank(message = "Purpose of reservation is required")
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
