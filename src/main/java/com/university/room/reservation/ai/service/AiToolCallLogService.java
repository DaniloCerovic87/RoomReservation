package com.university.room.reservation.ai.service;

public interface AiToolCallLogService {

    void logToolCall(String conversationId,
                     Long userId,
                     String toolName,
                     Object input,
                     Object output,
                     boolean success,
                     String errorMessage);
}
