package com.university.RoomReservation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import static com.university.RoomReservation.constants.MessageProperties.*;

@Data
@Builder
public class RoomRequest {

    @NotBlank(message = ROOM_NAME_NOT_BLANK)
    private String name;

    @NotBlank(message = ROOM_TYPE_NOT_BLANK)
    private String roomType;

    @NotNull(message = ROOM_CAPACITY_NOT_NULL)
    @Positive(message = ROOM_CAPACITY_POSITIVE)
    private Integer capacity;

    private Integer numberOfComputers;
}