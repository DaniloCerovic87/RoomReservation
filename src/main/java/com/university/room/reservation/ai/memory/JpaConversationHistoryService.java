package com.university.room.reservation.ai.memory;

import com.university.room.reservation.ai.context.AiConversationContext;
import com.university.room.reservation.model.AiConversation;
import com.university.room.reservation.model.AiConversationHistoryMessage;
import com.university.room.reservation.repository.AiConversationHistoryMessageRepository;
import com.university.room.reservation.repository.AiConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JpaConversationHistoryService {

    private final AiConversationRepository conversationRepository;
    private final AiConversationHistoryMessageRepository historyRepository;
    private final AiConversationContext conversationContext;
    private final AiMessageMapper messageMapper;

    @Transactional
    public void append(String conversationId, List<Message> messages) {
        if (messages.isEmpty()) {
            return;
        }

        AiConversation conversation = getOrCreateConversation(conversationId);
        long nextSequence = historyRepository
                .findTopByConversationOrderBySequenceNumberDesc(conversation)
                .map(message -> message.getSequenceNumber() + 1)
                .orElse(0L);

        List<AiConversationHistoryMessage> entities = new ArrayList<>();
        for (Message message : messages) {
            AiMessageMapper.StoredMessage stored = messageMapper.toStoredMessage(message);
            entities.add(AiConversationHistoryMessage.builder()
                    .conversation(conversation)
                    .sequenceNumber(nextSequence++)
                    .messageType(stored.messageType())
                    .content(stored.content())
                    .payload(stored.payload())
                    .createdAt(LocalDateTime.now())
                    .build());
        }

        historyRepository.saveAll(entities);
        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);
    }

    private AiConversation getOrCreateConversation(String conversationId) {
        return conversationRepository.findByConversationId(conversationId)
                .orElseGet(() -> conversationRepository.save(AiConversation.builder()
                        .conversationId(conversationId)
                        .userId(conversationContext.getUserId().orElse(null))
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()));
    }
}
