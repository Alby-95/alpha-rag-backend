package com.example.alpha.api;

import com.example.alpha.domain.Message;
import java.time.Instant;

public record MessageResponse(Long id, String role, String content, Instant createdAt) {
    public static MessageResponse from(Message m) {
        return new MessageResponse(m.getId(), m.getRole().name(), m.getContent(), m.getCreatedAt());
    }
}
