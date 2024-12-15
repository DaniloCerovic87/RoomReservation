package com.university.RoomReservation.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final String messageKey;

    public ResourceNotFoundException(String messageKey) {
        this.messageKey = messageKey;
    }
}
