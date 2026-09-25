package com.university.room.reservation.ai.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class RoomSearchRequest {

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer capacity;

}
