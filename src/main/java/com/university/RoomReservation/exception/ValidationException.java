package com.university.RoomReservation.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private final String messageKey;

    private final Object[] params;

    public ValidationException(String messageKey) {
        this.messageKey = messageKey;
        this.params = new Object[]{};
    }

    public ValidationException(String messageKey, Object... params) {
        this.messageKey = messageKey;
        this.params = params;
    }
}
