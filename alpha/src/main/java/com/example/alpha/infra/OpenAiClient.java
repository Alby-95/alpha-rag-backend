package com.example.alpha.infra;

import com.example.alpha.domain.AiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
public class OpenAiClient implements AiClient {

    private final WebClient webClient;
    private final String model;

    public OpenAiClient(
            @Value("${openai.api-key}") String apiKey,
            @Value("${openai.model:gpt-4o-mini}") String model
    ) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY is missing");
        }

        this.model = model;
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    @Override
    public String reply(Long conversationId, String userMessage) {
        try {
            Map<String, Object> body = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of("role", "system", "content", "You are a helpful assistant. Answer in Korean."),
                            Map.of("role", "user", "content", userMessage)
                    ),
                    "temperature", 0.7
            );

            Map<?, ?> res = webClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(20))
                    .block();

            if (res == null) return "AI 응답 없음";

            var choices = (List<?>) res.get("choices");
            if (choices == null || choices.isEmpty()) return "AI 응답 없음";

            var message = (Map<?, ?>) ((Map<?, ?>) choices.get(0)).get("message");
            return message.get("content").toString();

        } catch (org.springframework.web.reactive.function.client.WebClientResponseException.TooManyRequests e) {
            return "[AI 응답 실패: 요청 한도 초과] (더미 모드로 진행 중)";
        } catch (Exception e) {
            return "[AI 응답 실패] (결제/네트워크 설정 확인 필요)";
        }
    }
}