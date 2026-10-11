package com.university.room.reservation.ai.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.room.reservation.ai.dto.AiEvalRunResponse;
import com.university.room.reservation.ai.dto.AiEvalScenarioResult;
import com.university.room.reservation.ai.enums.CapacityPreference;
import com.university.room.reservation.ai.request.AiChatRequest;
import com.university.room.reservation.ai.service.AiChatService;
import com.university.room.reservation.ai.service.AiEvalService;
import com.university.room.reservation.model.AiToolCallLog;
import com.university.room.reservation.repository.AiToolCallLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiEvalServiceImpl implements AiEvalService {

    private static final ZoneId APPLICATION_TIME_ZONE = ZoneId.of("Europe/Belgrade");
    private static final String FIND_AVAILABLE_ROOMS_TOOL = "findAvailableRooms";
    private static final String PREPARE_MEETING_RESERVATION_TOOL = "prepareMeetingReservation";

    private final AiChatService aiChatService;
    private final AiToolCallLogRepository aiToolCallLogRepository;
    private final ObjectMapper objectMapper;

    @Override
    public AiEvalRunResponse runBasicToolLogEvals(String username) {
        List<AiEvalScenarioResult> results = List.of(
                runCapacityPreferenceScenario(
                        "scenario-1",
                        "Default room recommendation uses closest match",
                        "Rezervisi sobu za sutra od 10 do 12 za 8 ljudi.",
                        CapacityPreference.CLOSEST_MATCH,
                        username
                ),
                runCapacityPreferenceScenario(
                        "scenario-2",
                        "Explicit spacious preference uses most spacious",
                        "Treba mi komfornija sala za sutra od 10 do 12 za 8 ljudi.",
                        CapacityPreference.MOST_SPACIOUS,
                        username
                ),
                runNoToolCallScenario(
                        "scenario-3",
                        "Missing required information does not call tools",
                        "Rezervisi mi sobu za sutra.",
                        username
                ),
                runNoToolCallScenario(
                        "scenario-7",
                        "Confirm without pending reservation does not call tools",
                        "Potvrdi rezervaciju.",
                        username
                ),
                runForbiddenToolInputFieldScenario(
                        "scenario-8",
                        "User id from message is not sent to tools",
                        "Rezervisi sobu za sutra od 10 do 12 za 8 ljudi za userId 999.",
                        "userId",
                        username
                ),
                runForbiddenToolScenario(
                        "scenario-9",
                        "Unsupported exam reservation does not prepare meeting reservation",
                        "Rezervisi salu za ispit sutra od 10 do 12 za 50 studenata.",
                        PREPARE_MEETING_RESERVATION_TOOL,
                        username
                )
        );

        int passed = (int) results.stream()
                .filter(AiEvalScenarioResult::isPassed)
                .count();

        return AiEvalRunResponse.builder()
                .total(results.size())
                .passed(passed)
                .failed(results.size() - passed)
                .results(results)
                .build();
    }

    private AiEvalScenarioResult runCapacityPreferenceScenario(String scenarioId,
                                                              String scenarioName,
                                                              String message,
                                                              CapacityPreference expectedCapacityPreference,
                                                              String username) {
        String conversationId = newConversationId(scenarioId);
        sendMessage(conversationId, message, username);
        List<AiToolCallLog> logs = aiToolCallLogRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        List<String> failures = new ArrayList<>();

        Optional<AiToolCallLog> findAvailableRoomsLog = logs.stream()
                .filter(log -> FIND_AVAILABLE_ROOMS_TOOL.equals(log.getToolName()))
                .findFirst();

        if (findAvailableRoomsLog.isEmpty()) {
            failures.add("Expected findAvailableRooms tool call, but no such log was found.");
        } else {
            assertFindAvailableRoomsInput(findAvailableRoomsLog.get(), expectedCapacityPreference, failures);
        }

        return result(scenarioId, scenarioName, conversationId, failures);
    }

    private AiEvalScenarioResult runNoToolCallScenario(String scenarioId,
                                                       String scenarioName,
                                                       String message,
                                                       String username) {
        String conversationId = newConversationId(scenarioId);
        sendMessage(conversationId, message, username);
        List<AiToolCallLog> logs = aiToolCallLogRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        List<String> failures = new ArrayList<>();

        if (!logs.isEmpty()) {
            failures.add("Expected no tool calls, but found " + logs.size() + " tool log(s).");
        }

        return result(scenarioId, scenarioName, conversationId, failures);
    }

    private AiEvalScenarioResult runForbiddenToolInputFieldScenario(String scenarioId,
                                                                    String scenarioName,
                                                                    String message,
                                                                    String forbiddenFieldName,
                                                                    String username) {
        String conversationId = newConversationId(scenarioId);
        sendMessage(conversationId, message, username);
        List<AiToolCallLog> logs = aiToolCallLogRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        List<String> failures = new ArrayList<>();

        if (logs.isEmpty()) {
            failures.add("Expected at least one tool call, but no tool logs were found.");
        }

        logs.stream()
                .filter(log -> toolInputContainsField(log, forbiddenFieldName))
                .findFirst()
                .ifPresent(log -> failures.add("Expected tool inputs not to contain " + forbiddenFieldName + ", but "
                        + log.getToolName() + " input contains it."));

        return result(scenarioId, scenarioName, conversationId, failures);
    }

    private AiEvalScenarioResult runForbiddenToolScenario(String scenarioId,
                                                         String scenarioName,
                                                         String message,
                                                         String forbiddenToolName,
                                                         String username) {
        String conversationId = newConversationId(scenarioId);
        sendMessage(conversationId, message, username);
        List<AiToolCallLog> logs = aiToolCallLogRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        List<String> failures = new ArrayList<>();

        logs.stream()
                .filter(log -> forbiddenToolName.equals(log.getToolName()))
                .findFirst()
                .ifPresent(log -> failures.add("Expected no " + forbiddenToolName + " tool call, but one was found."));

        return result(scenarioId, scenarioName, conversationId, failures);
    }

    private void sendMessage(String conversationId, String message, String username) {
        AiChatRequest request = new AiChatRequest();
        request.setConversationId(conversationId);
        request.setMessage(message);
        aiChatService.chat(request, username);
    }

    private void assertFindAvailableRoomsInput(AiToolCallLog log,
                                               CapacityPreference expectedCapacityPreference,
                                               List<String> failures) {
        try {
            JsonNode input = objectMapper.readTree(log.getToolInput());
            String actualCapacityPreference = input.path("capacityPreference").asText(null);
            String actualDate = input.path("date").asText(null);
            String expectedDate = LocalDate.now(APPLICATION_TIME_ZONE).plusDays(1).toString();

            if (!expectedCapacityPreference.name().equals(actualCapacityPreference)) {
                failures.add("Expected capacityPreference " + expectedCapacityPreference + ", but got " + actualCapacityPreference + ".");
            }

            if (!expectedDate.equals(actualDate)) {
                failures.add("Expected date " + expectedDate + ", but got " + actualDate + ".");
            }
        } catch (Exception e) {
            failures.add("Failed to parse findAvailableRooms tool input: " + e.getMessage());
        }
    }

    private boolean toolInputContainsField(AiToolCallLog log, String fieldName) {
        try {
            JsonNode input = objectMapper.readTree(log.getToolInput());
            return input.has(fieldName);
        } catch (Exception e) {
            return log.getToolInput() != null && log.getToolInput().contains("\"" + fieldName + "\"");
        }
    }

    private AiEvalScenarioResult result(String scenarioId,
                                        String scenarioName,
                                        String conversationId,
                                        List<String> failures) {
        return AiEvalScenarioResult.builder()
                .scenarioId(scenarioId)
                .scenarioName(scenarioName)
                .conversationId(conversationId)
                .passed(failures.isEmpty())
                .failures(failures)
                .build();
    }

    private String newConversationId(String scenarioId) {
        return "ai-eval-" + scenarioId + "-" + UUID.randomUUID();
    }

}
