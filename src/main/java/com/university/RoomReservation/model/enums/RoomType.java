package com.university.RoomReservation.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoomType {
    CLASSROOM("Classroom"),
    COMPUTER_ROOM("Computer Room"),
    AMPHITHEATER("Amphitheater");

    private final String value;

}