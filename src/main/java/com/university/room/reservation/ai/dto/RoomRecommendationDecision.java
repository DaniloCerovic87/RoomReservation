package com.university.room.reservation.ai.dto;

public record RoomRecommendationDecision(
        RecommendationCriteria criteria,
        RoomRecommendationResponse response
) {
}
