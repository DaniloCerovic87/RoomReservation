package com.university.room.reservation.ai.memory;

import com.university.room.reservation.ai.context.AiConversationContext;
import com.university.room.reservation.model.AiConversation;
import com.university.room.reservation.repository.AiConversationHistoryMessageRepository;
import com.university.room.reservation.repository.AiConversationRepository;
import com.university.room.reservation.repository.AiConversationMessageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PersistentChatMemoryIntegrationTest {

    @Autowired
    private JpaConversationHistoryService historyService;

    @Autowired
    private JpaChatMemoryRepository memoryRepository;

    @Autowired
    private AiConversationRepository conversationRepository;

    @Autowired
    private AiConversationMessageRepository memoryMessageRepository;

    @Autowired
    private AiConversationHistoryMessageRepository historyMessageRepository;

    @Autowired
    private AiConversationContext conversationContext;

    private String conversationId;

    @AfterEach
    void cleanUp() {
        if (conversationId != null) {
            memoryRepository.deleteByConversationId(conversationId);
        }
        conversationContext.clear();
    }

    @Test
    void newChatMemoryInstanceLoadsMessagesPersistedByPreviousInstance() {
        conversationId = "persistence-test-" + UUID.randomUUID();
        List<Message> messages = List.of(
                new UserMessage("Želim da testiram trajnu memoriju."),
                new AssistantMessage("Memorija je sačuvana u bazi.")
        );

        ChatMemory firstMemory = newMemoryInstance();
        firstMemory.add(conversationId, messages);

        ChatMemory secondMemory = newMemoryInstance();
        List<Message> loadedMessages = secondMemory.get(conversationId);

        assertThat(loadedMessages)
                .extracting(Message::getText)
                .containsExactly(
                        "Želim da testiram trajnu memoriju.",
                        "Memorija je sačuvana u bazi."
                );

        AiConversation conversation = conversationRepository.findByConversationId(conversationId)
                .orElseThrow();

        assertThat(memoryMessageRepository.findByConversationOrderBySequenceNumberAsc(conversation))
                .hasSize(2);
        assertThat(historyMessageRepository.findByConversationOrderBySequenceNumberAsc(conversation))
                .hasSize(2);
    }

    private ChatMemory newMemoryInstance() {
        ChatMemory windowMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(memoryRepository)
                .maxMessages(20)
                .build();

        return new PersistentChatMemory(historyService, windowMemory);
    }
}
