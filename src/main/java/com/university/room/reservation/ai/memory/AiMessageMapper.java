package com.university.room.reservation.ai.memory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AiMessageMapper {

    private final ObjectMapper objectMapper;

    public AiMessageMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public StoredMessage toStoredMessage(Message message) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("metadata", message.getMetadata());

        if (message instanceof AssistantMessage assistantMessage) {
            payload.put("toolCalls", assistantMessage.getToolCalls());
        } else if (message instanceof ToolResponseMessage toolResponseMessage) {
            payload.put("responses", toolResponseMessage.getResponses());
        }

        try {
            return new StoredMessage(
                    message.getMessageType().name(),
                    message.getText(),
                    objectMapper.writeValueAsString(payload)
            );
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not serialize AI conversation message", exception);
        }
    }

    public Message toMessage(String messageType, String content, String payloadJson) {
        Map<String, Object> payload = readPayload(payloadJson);
        Map<String, Object> metadata = metadata(payload);

        return switch (messageType) {
            case "USER" -> new UserMessage(content);
            case "SYSTEM" -> new SystemMessage(content);
            case "ASSISTANT" -> new AssistantMessage(
                    content,
                    metadata,
                    objectMapper.convertValue(
                            payload.getOrDefault("toolCalls", List.of()),
                            new TypeReference<List<AssistantMessage.ToolCall>>() {
                            }
                    )
            );
            case "TOOL" -> new ToolResponseMessage(
                    objectMapper.convertValue(
                            payload.getOrDefault("responses", List.of()),
                            new TypeReference<List<ToolResponseMessage.ToolResponse>>() {
                            }
                    ),
                    metadata
            );
            default -> throw new IllegalStateException("Unsupported AI message type: " + messageType);
        };
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> metadata(Map<String, Object> payload) {
        Object metadata = payload.get("metadata");
        return metadata instanceof Map<?, ?> map
                ? (Map<String, Object>) map
                : Map.of();
    }

    private Map<String, Object> readPayload(String payload) {
        if (payload == null || payload.isBlank()) {
            return Map.of();
        }

        try {
            return objectMapper.readValue(payload, new TypeReference<>() {
            });
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not deserialize AI conversation message", exception);
        }
    }

    public record StoredMessage(String messageType, String content, String payload) {
    }
}
