package com.university.RoomReservation.exception;

public class EntityExistsException extends RuntimeException {

    public EntityExistsException(String message) {
        super(message);
    }
}
