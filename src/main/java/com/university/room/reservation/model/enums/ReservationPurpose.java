package com.university.room.reservation.model.enums;

import com.university.room.reservation.constants.MessageProperties;
import com.university.room.reservation.exception.ValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationPurpose {
    CLASS("Class"),
    EXAM("Exam"),
    MEETING("Meeting"),
    EVENT("Event");

    private final String value;

    public static ReservationPurpose fromValue(String value) {
        for (ReservationPurpose purpose : ReservationPurpose.values()) {
            if (purpose.value.equalsIgnoreCase(value)) {
                return purpose;
            }
        }
        throw new ValidationException(MessageProperties.UNKNOWN_RESERVATION_PURPOSE, value);
    }
}