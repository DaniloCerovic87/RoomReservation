package com.university.room.reservation.ai.service.impl;

import com.university.room.reservation.ai.dto.RoomRecommendationResponse;
import com.university.room.reservation.ai.service.RoomRecommendationService;
import com.university.room.reservation.dto.RoomDTO;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RoomRecommendationServiceImpl implements RoomRecommendationService {

    @Override
    public RoomRecommendationResponse recommendRoom(List<RoomDTO> availableRooms, Integer requestedCapacity) {
        if (availableRooms == null || availableRooms.isEmpty()) {
            return RoomRecommendationResponse.builder()
                    .recommendedRoom(null)
                    .reason("No available rooms to recommend.")
                    .alternatives(List.of())
                    .build();
        }

        List<RoomDTO> sortedRooms = availableRooms.stream()
                .filter(room -> requestedCapacity == null || room.getCapacity() >= requestedCapacity)
                .sorted(Comparator.comparingInt(room -> room.getCapacity() - requestedCapacity))
                .toList();

        if (sortedRooms.isEmpty()) {
            return RoomRecommendationResponse.builder()
                    .recommendedRoom(null)
                    .reason("No available rooms match the requested capacity.")
                    .alternatives(List.of())
                    .build();
        }

        RoomDTO recommendedRoom = sortedRooms.get(0);
        List<RoomDTO> alternatives = sortedRooms.stream()
                .skip(1)
                .toList();

        return RoomRecommendationResponse.builder()
                .recommendedRoom(recommendedRoom)
                .reason(buildReason(recommendedRoom, requestedCapacity))
                .alternatives(alternatives)
                .build();
    }

    private String buildReason(RoomDTO room, Integer requestedCapacity) {
        int unusedCapacity = room.getCapacity() - requestedCapacity;

        return "%s is recommended because it has enough capacity for %d people with the smallest unused capacity of %d seats."
                .formatted(room.getName(), requestedCapacity, unusedCapacity);
    }

}
