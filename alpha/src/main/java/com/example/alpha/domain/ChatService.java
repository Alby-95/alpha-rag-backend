package com.example.alpha.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final AiClient aiClient;

    public ChatService(ConversationRepository conversationRepository, MessageRepository messageRepository, AiClient aiClient) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.aiClient = aiClient;
    }

    @Transactional
    public Conversation createConversation() {
        return conversationRepository.save(new Conversation("새 대화"));
    }

    @Transactional
    public List<Message> sendEcho(Long conversationId, String content) {
        Conversation conv = conversationRepository.findById(conversationId).orElseThrow(() -> new IllegalArgumentException("Conversation not found: " + conversationId));

        messageRepository.save(new Message(conv, Message.Role.USER, content));

        String aiReply = aiClient.reply(conversationId, content);

        messageRepository.save(new Message(conv, Message.Role.ASSISTANT, aiReply));

        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    @Transactional(readOnly = true)
    public List<Message> getMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    @Transactional(readOnly = true)
    public List<Conversation> listConversations() {
        return conversationRepository.findAllByOrderByCreatedAtDesc();
    }
}
