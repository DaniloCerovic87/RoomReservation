package com.university.room.reservation.ai.memory;

import com.university.room.reservation.ai.context.AiConversationContext;
import com.university.room.reservation.model.AiConversation;
import com.university.room.reservation.model.AiConversationMessage;
import com.university.room.reservation.repository.AiConversationMessageRepository;
import com.university.room.reservation.repository.AiConversationRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaChatMemoryRepository implements ChatMemoryRepository {

    private final AiConversationRepository conversationRepository;
    private final AiConversationMessageRepository messageRepository;
    private final AiConversationContext conversationContext;
    private final AiMessageMapper messageMapper;

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public List<String> findConversationIds() {
        return conversationRepository.findAll().stream()
                .map(AiConversation::getConversationId)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public List<Message> findByConversationId( @NonNull String conversationId) {
        return conversationRepository.findByConversationId(conversationId)
                .map(messageRepository::findByConversationOrderBySequenceNumberAsc)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(this::toMessage)
                .toList();
    }

    @Override
    @Transactional
    public void saveAll( @NonNull String conversationId, List<Message> messages) {
        AiConversation conversation = getOrCreateConversation(conversationId);
        messageRepository.deleteByConversation(conversation);

        List<AiConversationMessage> entities = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            Message message = messages.get(i);
            AiMessageMapper.StoredMessage stored = messageMapper.toStoredMessage(message);
            entities.add(AiConversationMessage.builder()
                    .conversation(conversation)
                    .sequenceNumber((long) i)
                    .messageType(stored.messageType())
                    .content(stored.content())
                    .payload(stored.payload())
                    .createdAt(LocalDateTime.now())
                    .build());
        }

        messageRepository.saveAll(entities);
        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);
    }

    @Override
    @Transactional
    public void deleteByConversationId(@NonNull String conversationId) {
        conversationRepository.findByConversationId(conversationId)
                .ifPresent(conversation -> {
                    messageRepository.deleteByConversation(conversation);
                    conversationRepository.delete(conversation);
                });
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

    private Message toMessage(AiConversationMessage entity) {
        return messageMapper.toMessage(entity.getMessageType(), entity.getContent(), entity.getPayload());
    }
}
