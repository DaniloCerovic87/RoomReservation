package com.university.room.reservation.ai.service.impl;

import com.university.room.reservation.ai.agent.ReservationAgent;
import com.university.room.reservation.ai.request.AiChatRequest;
import com.university.room.reservation.ai.service.AiChatService;
import com.university.room.reservation.ai.store.ConversationUserStore;
import com.university.room.reservation.constants.MessageProperties;
import com.university.room.reservation.exception.ResourceNotFoundException;
import com.university.room.reservation.model.User;
import com.university.room.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final ReservationAgent reservationAgent;
    private final UserRepository userRepository;
    private final ConversationUserStore conversationUserStore;

    @Override
    public String chat(AiChatRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(MessageProperties.USER_NOT_FOUND));

        conversationUserStore.save(request.getConversationId(), user.getId());

        return reservationAgent.chat(request.getConversationId(), request.getMessage());
    }

}
