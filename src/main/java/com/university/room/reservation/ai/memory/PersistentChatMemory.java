package com.university.room.reservation.ai.memory;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public class PersistentChatMemory implements ChatMemory {

    private final JpaConversationHistoryService historyService;
    private final ChatMemory windowMemory;

    public PersistentChatMemory(JpaConversationHistoryService historyService,
                                @Qualifier("windowChatMemory") ChatMemory windowMemory) {
        this.historyService = historyService;
        this.windowMemory = windowMemory;
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        historyService.append(conversationId, messages);
        windowMemory.add(conversationId, messages);
    }

    @Override
    public List<Message> get(String conversationId) {
        return windowMemory.get(conversationId);
    }

    @Override
    public void clear(String conversationId) {
        windowMemory.clear(conversationId);
    }
}
