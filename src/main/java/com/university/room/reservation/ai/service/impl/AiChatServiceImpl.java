package com.university.room.reservation.ai.service.impl;

import com.university.room.reservation.ai.agent.ReservationAgent;
import com.university.room.reservation.ai.context.AiConversationContext;
import com.university.room.reservation.ai.request.AiChatRequest;
import com.university.room.reservation.ai.service.AiChatService;
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
    private final AiConversationContext aiConversationContext;

    @Override
    public String chat(AiChatRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(MessageProperties.USER_NOT_FOUND));

        try {
            aiConversationContext.set(request.getConversationId(), user.getId());
            return reservationAgent.chat(request.getConversationId(), request.getMessage());
        } finally {
            aiConversationContext.clear();
        }
    }

}
