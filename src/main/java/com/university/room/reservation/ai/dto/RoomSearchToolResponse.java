package com.university.room.reservation.ai.dto;

import com.university.room.reservation.dto.RoomDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class RoomSearchToolResponse {

    private boolean success;

    private List<RoomDTO> rooms;

    private String errorMessage;
}