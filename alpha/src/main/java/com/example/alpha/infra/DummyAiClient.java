package com.example.alpha.infra;

import com.example.alpha.domain.AiClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class DummyAiClient implements AiClient {

    @Override
    public String reply(Long conversationId, String userMessage) {
        return "더미 AI 응답입니다. 입력하신 메시지: " + userMessage;
    }
}