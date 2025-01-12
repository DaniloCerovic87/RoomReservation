package com.university.room.reservation.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmployeeFileStatus {

    IN_PROGRESS("IN_PROGRESS"),
    FAILED("FAILED"),
    FINISHED("FINISHED");

    private final String name;

}
