package com.university.RoomReservation.model.enums;

import com.university.RoomReservation.constants.MessageProperties;
import com.university.RoomReservation.exception.ValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String value;

    public static ReservationStatus fromValue(String value) {
        for (ReservationStatus status : ReservationStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new ValidationException(MessageProperties.UNKNOWN_RESERVATION_STATUS, value);
    }

}
