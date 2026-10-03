package com.university.room.reservation.ai.service.impl;

import com.university.room.reservation.ai.dto.RoomRecommendationResponse;
import com.university.room.reservation.ai.enums.CapacityPreference;
import com.university.room.reservation.ai.service.RoomRecommendationService;
import com.university.room.reservation.dto.RoomDTO;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RoomRecommendationServiceImpl implements RoomRecommendationService {

    @Override
    public RoomRecommendationResponse recommendRoom(List<RoomDTO> availableRooms,
                                                    Integer requestedCapacity,
                                                    CapacityPreference capacityPreference) {

        if (availableRooms == null || availableRooms.isEmpty()) {
            return RoomRecommendationResponse.builder()
                    .recommendedRoom(null)
                    .reason("No available rooms to recommend.")
                    .alternatives(List.of())
                    .build();
        }

        CapacityPreference effectivePreference = capacityPreference == null
                ? CapacityPreference.CLOSEST_MATCH
                : capacityPreference;

        List<RoomDTO> suitableRooms = availableRooms.stream()
                .filter(room -> room.getCapacity() != null)
                .filter(room -> requestedCapacity == null || room.getCapacity() >= requestedCapacity)
                .toList();

        if (suitableRooms.isEmpty()) {
            return RoomRecommendationResponse.builder()
                    .recommendedRoom(null)
                    .reason("No available rooms match the requested capacity.")
                    .alternatives(List.of())
                    .build();
        }

        List<RoomDTO> sortedRooms = sortRooms(suitableRooms, requestedCapacity, effectivePreference);

        RoomDTO recommendedRoom = sortedRooms.get(0);
        List<RoomDTO> alternatives = sortedRooms.stream()
                .skip(1)
                .toList();

        return RoomRecommendationResponse.builder()
                .recommendedRoom(recommendedRoom)
                .reason(buildReason(recommendedRoom, requestedCapacity, effectivePreference))
                .alternatives(alternatives)
                .build();
    }

    private List<RoomDTO> sortRooms(
            List<RoomDTO> rooms,
            Integer requestedCapacity,
            CapacityPreference capacityPreference
    ) {
        return switch (capacityPreference) {
            case MOST_SPACIOUS -> rooms.stream()
                    .sorted(Comparator.comparing(RoomDTO::getCapacity).reversed())
                    .toList();
            case CLOSEST_MATCH -> rooms.stream()
                    .sorted(Comparator.comparingInt(room -> room.getCapacity() - requestedCapacity))
                    .toList();
        };
    }

    private String buildReason(RoomDTO room, Integer requestedCapacity, CapacityPreference capacityPreference) {
        int unusedCapacity = room.getCapacity() - requestedCapacity;
        return switch (capacityPreference) {
            case MOST_SPACIOUS -> "%s is recommended because it is the most spacious available room for %d people, with %d extra seats."
                    .formatted(room.getName(), requestedCapacity, unusedCapacity);
            case CLOSEST_MATCH -> "%s is recommended because it has enough capacity for %d people with the smallest unused capacity of %d seats."
                    .formatted(room.getName(), requestedCapacity, unusedCapacity);
        };
    }

}
