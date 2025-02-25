package com.university.room.reservation.request;

import com.university.room.reservation.constants.MessageProperties;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRequest {

    @NotNull(message = MessageProperties.RESERVATION_USER_ID_NOT_NULL)
    private Long userId;

    @NotNull(message = MessageProperties.RESERVATION_ROOM_ID_NOT_NULL)
    private Long roomId;

    @NotNull(message = MessageProperties.RESERVATION_START_TIME_NOT_NULL)
    @FutureOrPresent(message = MessageProperties.RESERVATION_START_TIME_FUTURE_OR_PRESENT)
    private LocalDateTime startTime;

    @NotNull(message = MessageProperties.RESERVATION_END_TIME_NOT_NULL)
    @FutureOrPresent(message = MessageProperties.RESERVATION_END_TIME_FUTURE_OR_PRESENT)
    private LocalDateTime endTime;

    @NotBlank(message = MessageProperties.RESERVATION_PURPOSE_NOT_BLANK)
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
