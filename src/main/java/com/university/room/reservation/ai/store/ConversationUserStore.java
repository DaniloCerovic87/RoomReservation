package com.university.room.reservation.ai.store;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConversationUserStore {

    private final Map<String, Long> conversationUsers = new ConcurrentHashMap<>();

    public void save(String conversationId, Long userId) {
        conversationUsers.put(conversationId, userId);
    }

    public Optional<Long> findUserIdByConversationId(String conversationId) {
        return Optional.ofNullable(conversationUsers.get(conversationId));
    }

    public void remove(String conversationId) {
        conversationUsers.remove(conversationId);
    }
}
