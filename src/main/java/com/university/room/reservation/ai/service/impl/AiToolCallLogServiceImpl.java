package com.university.room.reservation.ai.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.room.reservation.ai.service.AiToolCallLogService;
import com.university.room.reservation.model.AiToolCallLog;
import com.university.room.reservation.repository.AiToolCallLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiToolCallLogServiceImpl implements AiToolCallLogService {

    private final AiToolCallLogRepository aiToolCallLogRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void logToolCall(String conversationId,
                            Long userId,
                            String toolName,
                            Object input,
                            Object output,
                            boolean success,
                            String errorMessage) {
        try {
            AiToolCallLog aiToolCallLog = AiToolCallLog.builder()
                    .conversationId(conversationId)
                    .userId(userId)
                    .toolName(toolName)
                    .toolInput(toJson(input))
                    .toolOutput(toJson(output))
                    .success(success)
                    .errorMessage(errorMessage)
                    .build();

            aiToolCallLogRepository.save(aiToolCallLog);
        } catch (Exception e) {
            log.warn("Failed to persist AI tool call log for tool {}", toolName, e);
        }
    }

    private String toJson(Object value) {
        if (value == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize AI tool log value", e);
            return String.valueOf(value);
        }
    }
}
