package com.example.alpha.api;

import com.example.alpha.domain.Conversation;
import java.time.Instant;

public record ConversationResponse(
        Long id,
        String title,
        Instant createdAt
) {
    public static ConversationResponse from(Conversation c) {
        return new ConversationResponse(
                c.getId(),
                c.getTitle(),
                c.getCreatedAt()
        );
    }
}