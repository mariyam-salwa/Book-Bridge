package com.bookbridge.bookbridge.controller;

import com.bookbridge.bookbridge.ChatMessage;
import com.bookbridge.bookbridge.repository.ChatMessageRepository;
import com.bookbridge.bookbridge.repository.StudentRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatMessageRepository chatRepository;
    private final StudentRepository studentRepository;

    public ChatController(
            ChatMessageRepository chatRepository,
            StudentRepository studentRepository) {
        this.chatRepository = chatRepository;
        this.studentRepository = studentRepository;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @RequestBody ChatMessage message) {

        if (message.getSenderEmail() == null ||
            message.getReceiverEmail() == null ||
            message.getContent() == null ||
            message.getContent().isBlank()) {

            return ResponseEntity.badRequest()
                .body(Map.of("error", "All message details are required"));
        }

        if (message.getContent().length() > 2000) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Message is too long"));
        }

        if (!studentRepository.existsByEmail(message.getSenderEmail()) ||
            !studentRepository.existsByEmail(message.getReceiverEmail())) {

            return ResponseEntity.badRequest()
                .body(Map.of("error", "Student not found"));
        }

        message.setSentAt(LocalDateTime.now());

        return ResponseEntity.ok(chatRepository.save(message));
    }

    @GetMapping("/conversation")
    public ResponseEntity<?> getConversation(
            @RequestParam String user1,
            @RequestParam String user2) {

        if (!studentRepository.existsByEmail(user1) ||
            !studentRepository.existsByEmail(user2)) {

            return ResponseEntity.badRequest()
                .body(Map.of("error", "Student not found"));
        }

        List<ChatMessage> messages =
            chatRepository
                .findBySenderEmailAndReceiverEmailOrSenderEmailAndReceiverEmailOrderBySentAtAsc(
                    user1, user2, user2, user1
                );

        return ResponseEntity.ok(messages);
    }
}