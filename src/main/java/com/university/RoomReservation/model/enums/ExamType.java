package com.university.RoomReservation.model.enums;

import com.university.RoomReservation.constants.MessageProperties;
import com.university.RoomReservation.exception.ValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExamType {
    WRITTEN("Written"),
    ORAL("Oral"),
    SEMINAR("Seminar");

    private final String value;

    public static ExamType fromValue(String value) {
        for (ExamType type : ExamType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new ValidationException(MessageProperties.UNKNOWN_EXAM_TYPE, value);
    }
}