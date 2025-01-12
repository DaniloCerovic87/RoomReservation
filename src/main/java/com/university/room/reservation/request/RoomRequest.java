package com.university.room.reservation.request;

import com.university.room.reservation.constants.MessageProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomRequest {

    @NotBlank(message = MessageProperties.ROOM_NAME_NOT_BLANK)
    private String name;

    @NotBlank(message = MessageProperties.ROOM_TYPE_NOT_BLANK)
    private String roomType;

    @NotNull(message = MessageProperties.ROOM_CAPACITY_NOT_NULL)
    @Positive(message = MessageProperties.ROOM_CAPACITY_POSITIVE)
    private Integer capacity;

    private Integer numberOfComputers;
}