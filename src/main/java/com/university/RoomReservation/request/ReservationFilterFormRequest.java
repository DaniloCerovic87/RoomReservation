package com.university.RoomReservation.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationFilterFormRequest {

    private String roomName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private String reservationPurpose;

}
