package com.university.room.reservation.ai.service;

import com.university.room.reservation.model.enums.RoomType;

import java.util.Optional;

public interface HistoricalRoomPreferenceService {

    Optional<RoomType> findPreferredRoomType(Long userId);
}
