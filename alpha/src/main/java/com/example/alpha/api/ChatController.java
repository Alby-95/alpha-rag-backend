package com.example.alpha.api;

import com.example.alpha.domain.ChatService;
import com.example.alpha.domain.Conversation;
import com.example.alpha.domain.Message;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/conversations")
    public Conversation createConversation() {
        return chatService.createConversation();
    }

    @GetMapping("/conversations/{id}/messages")
    public List<MessageResponse> getMessages(@PathVariable Long id) {
        return chatService.getMessages(id).stream().map(MessageResponse::from).toList();
    }
    @GetMapping("/conversations")
    public List<ConversationResponse> listConversations() {
        return chatService.listConversations().stream()
                .map(ConversationResponse::from)
                .toList();
    }

    @PostMapping("/conversations/{id}/messages")
    public List<MessageResponse> sendMessage(@PathVariable Long id,
                                             @Valid @RequestBody SendMessageRequest req) {
        List<Message> messages = chatService.sendEcho(id, req.content());
        return messages.stream().map(MessageResponse::from).toList();
    }
}
