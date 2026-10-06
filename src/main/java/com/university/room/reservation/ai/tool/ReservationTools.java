package com.university.room.reservation.ai.tool;

import com.university.room.reservation.ai.dto.ConfirmMeetingReservationResponse;
import com.university.room.reservation.ai.dto.PendingMeetingReservation;
import com.university.room.reservation.ai.dto.RoomRecommendationResponse;
import com.university.room.reservation.ai.dto.RoomSearchToolResponse;
import com.university.room.reservation.ai.context.AiConversationContext;
import com.university.room.reservation.ai.request.RoomSearchRequest;
import com.university.room.reservation.ai.service.AiToolCallLogService;
import com.university.room.reservation.ai.service.RoomRecommendationService;
import com.university.room.reservation.ai.store.PendingMeetingReservationStore;
import com.university.room.reservation.constants.MessageProperties;
import com.university.room.reservation.dto.ReservationDTO;
import com.university.room.reservation.dto.RoomDTO;
import com.university.room.reservation.exception.ResourceNotFoundException;
import com.university.room.reservation.exception.ValidationException;
import com.university.room.reservation.model.enums.ReservationPurpose;
import com.university.room.reservation.request.ReservationRequest;
import com.university.room.reservation.service.ReservationService;
import com.university.room.reservation.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationTools {

    private final RoomService roomService;
    private final MessageSource messageSource;
    private final PendingMeetingReservationStore pendingMeetingReservationStore;
    private final ReservationService reservationService;
    private final RoomRecommendationService roomRecommendationService;
    private final AiToolCallLogService aiToolCallLogService;
    private final AiConversationContext aiConversationContext;

    @Tool(description = """
            Finds available meeting rooms for a given date, time range, capacity, and capacity preference.                                                                                                                                                                                                                   \s
                                                                                                                                                                                                                                                                                                                              \s
            Use this tool only when the user provided:                                                                                                                                                                                                                                                                       \s
             - date                                                                                                                                                                                                                                                                                                           \s
             - start time                                                                                                                                                                                                                                                                                                     \s
             - end time                                                                                                                                                                                                                                                                                                       \s
             - capacity                                                                                                                                                                                                                                                                                                       \s
                                                                                                                                                                                                                                                                                                                              \s
            When calling this tool, infer capacityPreference from the user's message.                                                                                                                                                                                                                                        \s
                                                                                                                                                                                                                                                                                                                              \s
            Set capacityPreference to MOST_SPACIOUS only when the user explicitly asks for a larger, more comfortable, spacious, roomy, not-too-tight, biggest, or largest room.                                                                                                                                             \s
            Serbian examples for MOST_SPACIOUS include: "komfornija sala", "prostranija sala", "veća sala", "najveća sala", "da ne bude knap".                                                                                                                 \s
                                                                                                                                                                                                                                                                                                                              \s
            Set capacityPreference to CLOSEST_MATCH when the user asks for the smallest suitable room, closest fit, best capacity match, least unused capacity, or does not specify any capacity preference.                                                                                                                 \s
            Serbian examples for CLOSEST_MATCH include: "najmanja odgovarajuća sala", "najbliža po kapacitetu", or no capacity preference.                                                                                                                     \s
            Do not set MOST_SPACIOUS just because the user asks for a room/sala/soba. Room, sala, and soba are normal nouns, not spaciousness preferences.                                                                                                      \s
                                                                                                                                                                                                                                                                                                                              \s
            Do not mention capacityPreference to the user unless they ask for debugging details.                                                                                                                                                                                                                             \s
                                                                                                                                                                                                                                                                                                                              \s
            When available rooms are returned with a recommendation, present the recommended room first.                                                                                                                                                                                                                     \s
            Use the recommendation reason from the tool response.                                                                                                                                                                                                                                                            \s
            Then list the other available rooms as alternatives.                                                                                                                                                                                                                                                             \s
            Do not invent recommendation reasons.
            """)
    public RoomSearchToolResponse findAvailableRooms(RoomSearchRequest request) {
        String conversationId = resolveConversationId(request.getConversationId());
        request.setConversationId(conversationId);
        Long userId = resolveUserId();

        try {
            List<RoomDTO> rooms = roomService.findAvailableRooms(
                    request.getDate(),
                    request.getStartTime(),
                    request.getEndTime(),
                    request.getCapacity()
            );

            RoomRecommendationResponse recommendation =
                    roomRecommendationService.recommendRoom(rooms, request.getCapacity(), request.getCapacityPreference());

            RoomSearchToolResponse response = RoomSearchToolResponse.builder()
                    .success(true)
                    .rooms(rooms)
                    .recommendation(recommendation)
                    .build();

            logToolCall(conversationId, userId, "findAvailableRooms", request, response, true, null);

            return response;
        }  catch (ValidationException e) {
            String message = getMessage(e.getMessageKey(), e.getParams());

            RoomSearchToolResponse response = RoomSearchToolResponse.builder()
                    .success(false)
                    .rooms(List.of())
                    .errorMessage(message)
                    .build();

            logToolCall(conversationId, userId, "findAvailableRooms", request, response, false, message);

            return response;
        } catch (RuntimeException e) {
            logToolCall(conversationId, userId, "findAvailableRooms", request, null, false, e.getMessage());
            throw e;
        }
    }


    @Tool(description = """
          Prepares a pending meeting room reservation after the user selected a room.
          This tool does not create the reservation in the database.
          Use it only after the user selected a specific room and the reservation details are known.
          If a pending reservation already exists for the same conversationId, this tool replaces it.
          Use it again when the user corrects pending reservation details before confirmation.
          After this tool is called, ask the user for explicit confirmation.
          """)
    public PendingMeetingReservation prepareMeetingReservation(PendingMeetingReservation pendingMeetingReservation) {
        String conversationId = resolveConversationId(pendingMeetingReservation.getConversationId());
        pendingMeetingReservation.setConversationId(conversationId);

        pendingMeetingReservationStore.save(pendingMeetingReservation);
        logToolCall(
                conversationId,
                resolveUserId(),
                "prepareMeetingReservation",
                pendingMeetingReservation,
                pendingMeetingReservation,
                true,
                null
        );
        return pendingMeetingReservation;
    }

    @Tool(description = """
          Confirms and creates a previously prepared pending meeting room reservation.
          Use this tool only when the user explicitly confirms the pending reservation.
          Requires the internal conversationId for the current conversation.
          """)
    public ConfirmMeetingReservationResponse confirmMeetingReservation(String conversationId) {
        String resolvedConversationId = resolveConversationId(conversationId);
        Long userId = resolveUserId();

        try {
            ConfirmMeetingReservationResponse response = pendingMeetingReservationStore.findByConversationId(resolvedConversationId)
                    .map(this::createPendingMeetingReservation)
                    .orElseGet(() -> ConfirmMeetingReservationResponse.builder()
                            .success(false)
                            .errorMessage(getMessage(MessageProperties.AI_PENDING_MEETING_RESERVATION_NOT_FOUND))
                            .build());

            logToolCall(
                    resolvedConversationId,
                    userId,
                    "confirmMeetingReservation",
                    resolvedConversationId,
                    response,
                    response.isSuccess(),
                    response.getErrorMessage()
            );

            return response;
        } catch (RuntimeException e) {
            logToolCall(resolvedConversationId, userId, "confirmMeetingReservation", resolvedConversationId, null, false, e.getMessage());
            throw e;
        }
    }

    private ConfirmMeetingReservationResponse createPendingMeetingReservation(PendingMeetingReservation pendingMeetingReservation) {
        Long userId = aiConversationContext.getUserId()
                .orElse(null);

        if (userId == null) {
            return ConfirmMeetingReservationResponse.builder()
                    .success(false)
                    .errorMessage(getMessage(MessageProperties.AI_CONVERSATION_USER_NOT_FOUND))
                    .build();
        }

        ReservationRequest request = getReservationRequest(pendingMeetingReservation, userId);

        try {
            ReservationDTO reservation = reservationService.createReservation(request);
            pendingMeetingReservationStore.remove(pendingMeetingReservation.getConversationId());

            return ConfirmMeetingReservationResponse.builder()
                    .success(true)
                    .reservation(reservation)
                    .build();
        } catch (ValidationException e) {
            return ConfirmMeetingReservationResponse.builder()
                    .success(false)
                    .errorMessage(getMessage(e.getMessageKey(), e.getParams()))
                    .build();
        } catch (ResourceNotFoundException e) {
            return ConfirmMeetingReservationResponse.builder()
                    .success(false)
                    .errorMessage(getMessage(e.getMessageKey()))
                    .build();
        }
    }

    private static ReservationRequest getReservationRequest(PendingMeetingReservation pendingMeetingReservation, Long userId) {
        ReservationRequest request = new ReservationRequest();
        request.setUserId(userId);
        request.setRoomId(pendingMeetingReservation.getRoomId());
        request.setStartTime(parseLocalDateTime(pendingMeetingReservation.getStartTime()));
        request.setEndTime(parseLocalDateTime(pendingMeetingReservation.getEndTime()));
        request.setReservationPurpose(ReservationPurpose.MEETING.getValue());
        request.setMeetingName(pendingMeetingReservation.getMeetingName());
        request.setMeetingDescription(pendingMeetingReservation.getMeetingDescription());
        return request;
    }

    private static LocalDateTime parseLocalDateTime(String value) {
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException ignored) {
            try {
                return OffsetDateTime.parse(value).toLocalDateTime();
            } catch (DateTimeParseException ignoredAgain) {
                return ZonedDateTime.parse(value).toLocalDateTime();
            }
        }
    }

    private String getMessage(String messageKey, Object... params) {
        return messageSource.getMessage(
                messageKey,
                params,
                LocaleContextHolder.getLocale()
        );
    }

    private String resolveConversationId(String conversationId) {
        return conversationId != null ? conversationId : aiConversationContext.getConversationId().orElse(null);
    }

    private Long resolveUserId() {
        return aiConversationContext.getUserId()
                .orElse(null);
    }

    private void logToolCall(String conversationId,
                             Long userId,
                             String toolName,
                             Object input,
                             Object output,
                             boolean success,
                             String errorMessage) {
        aiToolCallLogService.logToolCall(conversationId, userId, toolName, input, output, success, errorMessage);
    }

}
