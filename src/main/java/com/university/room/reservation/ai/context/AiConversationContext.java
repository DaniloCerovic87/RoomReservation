package com.university.room.reservation.ai.context;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AiConversationContext {

    private final ThreadLocal<Context> currentContext = new ThreadLocal<>();

    public void set(String conversationId, Long userId) {
        currentContext.set(new Context(conversationId, userId));
    }

    public Optional<String> getConversationId() {
        return Optional.ofNullable(currentContext.get())
                .map(Context::conversationId);
    }

    public Optional<Long> getUserId() {
        return Optional.ofNullable(currentContext.get())
                .map(Context::userId);
    }

    public void clear() {
        currentContext.remove();
    }

    private record Context(String conversationId, Long userId) {
    }
}
