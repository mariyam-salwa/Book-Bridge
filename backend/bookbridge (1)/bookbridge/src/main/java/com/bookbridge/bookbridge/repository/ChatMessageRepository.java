package com.bookbridge.bookbridge.repository;

import com.bookbridge.bookbridge.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage>
    findBySenderEmailAndReceiverEmailOrSenderEmailAndReceiverEmailOrderBySentAtAsc(
        String sender1,
        String receiver1,
        String sender2,
        String receiver2
    );
}