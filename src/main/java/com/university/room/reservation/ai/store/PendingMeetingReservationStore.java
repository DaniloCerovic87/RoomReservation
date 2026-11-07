package com.university.room.reservation.ai.store;

import com.university.room.reservation.ai.dto.PendingMeetingReservation;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PendingMeetingReservationStore {

    private final Map<String, PendingMeetingReservation> pendingReservations = new ConcurrentHashMap<>();

    public void save(PendingMeetingReservation pendingMeetingReservation) {
        pendingReservations.put(pendingMeetingReservation.getConversationId(), pendingMeetingReservation);
    }

    public Optional<PendingMeetingReservation> findByConversationId(String conversationId) {
        return Optional.ofNullable(pendingReservations.get(conversationId));
    }

    public void remove(String conversationId) {
        pendingReservations.remove(conversationId);
    }
}