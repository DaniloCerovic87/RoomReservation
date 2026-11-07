package com.university.room.reservation.ai.service.impl;

import com.university.room.reservation.ai.agent.RecommendationAgent;
import com.university.room.reservation.ai.dto.RecommendationCriteria;
import com.university.room.reservation.ai.dto.RoomRecommendationDecision;
import com.university.room.reservation.ai.dto.RoomRecommendationResponse;
import com.university.room.reservation.ai.request.RoomSearchRequest;
import com.university.room.reservation.ai.service.HistoricalRoomPreferenceService;
import com.university.room.reservation.ai.service.RoomRecommendationCoordinator;
import com.university.room.reservation.ai.service.RoomRecommendationService;
import com.university.room.reservation.dto.RoomDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomRecommendationCoordinatorImpl implements RoomRecommendationCoordinator {

    private final RecommendationAgent recommendationAgent;
    private final HistoricalRoomPreferenceService historicalRoomPreferenceService;
    private final RoomRecommendationService roomRecommendationService;

    @Override
    public RoomRecommendationDecision recommend(List<RoomDTO> availableRooms,
                                                RoomSearchRequest request,
                                                Long userId) {
        RecommendationCriteria criteria = resolveCriteria(request, userId);
        RoomRecommendationResponse response = roomRecommendationService.recommendRoom(
                availableRooms,
                request.getCapacity(),
                criteria
        );

        return new RoomRecommendationDecision(criteria, response);
    }

    private RecommendationCriteria resolveCriteria(RoomSearchRequest request, Long userId) {
        RecommendationCriteria criteria = RecommendationCriteria.builder().build();

        if (request.getRecommendationQuery() != null && !request.getRecommendationQuery().isBlank()) {
            try {
                RecommendationCriteria interpreted = recommendationAgent.interpret(request.getRecommendationQuery());
                if (interpreted != null) {
                    criteria = interpreted;
                }
            } catch (RuntimeException exception) {
                log.warn("Recommendation agent failed, using deterministic fallback", exception);
            }
        }

        if (criteria.getPreferredRoomType() == null) {
            historicalRoomPreferenceService.findPreferredRoomType(userId)
                    .ifPresent(criteria::setPreferredRoomType);
        }

        return criteria;
    }

}
