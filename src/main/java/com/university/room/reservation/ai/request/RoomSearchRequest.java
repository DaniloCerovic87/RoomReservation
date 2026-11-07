package com.university.room.reservation.ai.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class RoomSearchRequest {

    private String conversationId;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer capacity;

    private String recommendationQuery;

}
