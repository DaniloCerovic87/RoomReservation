package com.university.room.reservation.ai.dto;

import com.university.room.reservation.dto.ReservationDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ConfirmMeetingReservationResponse {

    private boolean success;

    private ReservationDTO reservation;

    private String errorMessage;
}
