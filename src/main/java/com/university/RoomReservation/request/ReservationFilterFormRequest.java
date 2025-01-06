package com.university.RoomReservation.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationFilterFormRequest {

    private String roomName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String reservationPurpose;

}
