package com.university.room.reservation.ai.service;

import com.university.room.reservation.ai.dto.RoomRecommendationDecision;
import com.university.room.reservation.ai.request.RoomSearchRequest;
import com.university.room.reservation.dto.RoomDTO;

import java.util.List;

public interface RoomRecommendationCoordinator {

    RoomRecommendationDecision recommend(
            List<RoomDTO> availableRooms,
            RoomSearchRequest request,
            Long userId
    );
}
