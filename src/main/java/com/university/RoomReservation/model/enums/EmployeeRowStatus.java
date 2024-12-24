package com.university.RoomReservation.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmployeeRowStatus {

    NEW("NEW"),
    IN_PROGRESS("IN_PROGRESS"),
    SUCCESS("SUCCESS"),
    FAILED("FAILED");

    private final String name;

}
