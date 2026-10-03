package com.university.room.reservation.ai.tool;

import com.university.room.reservation.ai.dto.ConfirmMeetingReservationResponse;
import com.university.room.reservation.ai.dto.PendingMeetingReservation;
import com.university.room.reservation.ai.dto.RoomRecommendationResponse;
import com.university.room.reservation.ai.dto.RoomSearchToolResponse;
import com.university.room.reservation.ai.request.RoomSearchRequest;
import com.university.room.reservation.ai.service.RoomRecommendationService;
import com.university.room.reservation.ai.store.ConversationUserStore;
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

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationTools {

    private final RoomService roomService;
    private final MessageSource messageSource;
    private final PendingMeetingReservationStore pendingMeetingReservationStore;
    private final ConversationUserStore conversationUserStore;
    private final ReservationService reservationService;
    private final RoomRecommendationService roomRecommendationService;

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
            Set capacityPreference to MOST_SPACIOUS when the user asks for a larger, more comfortable, spacious, roomy, or not-too-tight room.                                                                                                                                                                               \s
                                                                                                                                                                                                                                                                                                                              \s
            Set capacityPreference to CLOSEST_MATCH when the user asks for the smallest suitable room, closest fit, best capacity match, least unused capacity, or does not specify any capacity preference.                                                                                                                 \s
                                                                                                                                                                                                                                                                                                                              \s
            Do not mention capacityPreference to the user unless they ask for debugging details.                                                                                                                                                                                                                             \s
                                                                                                                                                                                                                                                                                                                              \s
            When available rooms are returned with a recommendation, present the recommended room first.                                                                                                                                                                                                                     \s
            Use the recommendation reason from the tool response.                                                                                                                                                                                                                                                            \s
            Then list the other available rooms as alternatives.                                                                                                                                                                                                                                                             \s
            Do not invent recommendation reasons.
            """)
    public RoomSearchToolResponse findAvailableRooms(RoomSearchRequest request) {
        try {
            List<RoomDTO> rooms = roomService.findAvailableRooms(
                    request.getDate(),
                    request.getStartTime(),
                    request.getEndTime(),
                    request.getCapacity()
            );

            RoomRecommendationResponse recommendation =
                    roomRecommendationService.recommendRoom(rooms, request.getCapacity(), request.getCapacityPreference());

            return RoomSearchToolResponse.builder()
                    .success(true)
                    .rooms(rooms)
                    .recommendation(recommendation)
                    .build();
        }  catch (ValidationException e) {
                    String message = messageSource.getMessage(
                    e.getMessageKey(),
                    e.getParams(),
                    LocaleContextHolder.getLocale()
            );

            return RoomSearchToolResponse.builder()
                    .success(false)
                    .rooms(List.of())
                    .errorMessage(message)
                    .build();
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
        pendingMeetingReservationStore.save(pendingMeetingReservation);
        return pendingMeetingReservation;
    }

    @Tool(description = """
          Confirms and creates a previously prepared pending meeting room reservation.
          Use this tool only when the user explicitly confirms the pending reservation.
          Requires the internal conversationId for the current conversation.
          """)
    public ConfirmMeetingReservationResponse confirmMeetingReservation(String conversationId) {
        return pendingMeetingReservationStore.findByConversationId(conversationId)
                .map(this::createPendingMeetingReservation)
                .orElseGet(() -> ConfirmMeetingReservationResponse.builder()
                        .success(false)
                        .errorMessage(getMessage(MessageProperties.AI_PENDING_MEETING_RESERVATION_NOT_FOUND))
                        .build());
    }

    private ConfirmMeetingReservationResponse createPendingMeetingReservation(PendingMeetingReservation pendingMeetingReservation) {
        Long userId = conversationUserStore.findUserIdByConversationId(pendingMeetingReservation.getConversationId())
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
            conversationUserStore.remove(pendingMeetingReservation.getConversationId());

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
        request.setStartTime(pendingMeetingReservation.getStartTime());
        request.setEndTime(pendingMeetingReservation.getEndTime());
        request.setReservationPurpose(ReservationPurpose.MEETING.getValue());
        request.setMeetingName(pendingMeetingReservation.getMeetingName());
        request.setMeetingDescription(pendingMeetingReservation.getMeetingDescription());
        return request;
    }

    private String getMessage(String messageKey, Object... params) {
        return messageSource.getMessage(
                messageKey,
                params,
                LocaleContextHolder.getLocale()
        );
    }

}
