package com.university.room.reservation.ai.dto;

import com.university.room.reservation.dto.RoomDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRecommendationResponse {

    private RoomDTO recommendedRoom;
    private String reason;
    private List<RoomDTO> alternatives;

}
