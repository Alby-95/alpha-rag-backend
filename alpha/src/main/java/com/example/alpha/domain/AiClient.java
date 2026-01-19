package com.example.alpha.domain;

public interface AiClient {
    String reply(Long conversationId, String userMessage);
}