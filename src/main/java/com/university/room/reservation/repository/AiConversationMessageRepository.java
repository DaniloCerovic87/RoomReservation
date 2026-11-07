package com.university.room.reservation.repository;

import com.university.room.reservation.model.AiConversation;
import com.university.room.reservation.model.AiConversationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AiConversationMessageRepository extends JpaRepository<AiConversationMessage, Long> {

    List<AiConversationMessage> findByConversationOrderBySequenceNumberAsc(AiConversation conversation);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from AiConversationMessage message where message.conversation = :conversation")
    void deleteByConversation(@Param("conversation") AiConversation conversation);
}
