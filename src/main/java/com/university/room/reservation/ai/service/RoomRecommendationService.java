package com.university.room.reservation.ai.service;

import com.university.room.reservation.ai.dto.RoomRecommendationResponse;
import com.university.room.reservation.dto.RoomDTO;

import java.util.List;

public interface RoomRecommendationService {

    RoomRecommendationResponse recommendRoom(List<RoomDTO> availableRooms, Integer requestedCapacity);

}
