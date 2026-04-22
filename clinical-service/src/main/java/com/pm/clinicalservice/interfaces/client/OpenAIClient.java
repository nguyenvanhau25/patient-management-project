package com.pm.clinicalservice.interfaces.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenAIClient {
    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
    private String apiUrl;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank();
    }

    public String generateClinicalSuggestion(String systemPrompt, String userPrompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "temperature", 0.2,
                "response_format", Map.of("type", "json_object"),
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)));

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, Map.class);

        Object choicesObject = response.getBody() == null ? null : response.getBody().get("choices");
        if (!(choicesObject instanceof List<?> choices) || choices.isEmpty()) {
            throw new RuntimeException("Phản hồi từ OpenAI không chứa lựa chọn nào");
        }

        Object firstChoice = choices.get(0);
        if (!(firstChoice instanceof Map<?, ?> choiceMap)) {
            throw new RuntimeException("Định dạng lựa chọn trong phản hồi từ OpenAI không hợp lệ");
        }

        Object messageObject = choiceMap.get("message");
        if (!(messageObject instanceof Map<?, ?> messageMap)) {
            throw new RuntimeException("Định dạng tin nhắn trong phản hồi từ OpenAI không hợp lệ");
        }

        Object contentObject = messageMap.get("content");
        if (!(contentObject instanceof String content) || content.isBlank()) {
            throw new RuntimeException("Nội dung phản hồi từ OpenAI trống");
        }

        return content;
    }

    public String generateChatResponse(List<Map<String, String>> messages) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "temperature", 0.7,
                "messages", messages);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, Map.class);

        Object choicesObject = response.getBody() == null ? null : response.getBody().get("choices");
        if (!(choicesObject instanceof List<?> choices) || choices.isEmpty()) {
            throw new RuntimeException("Phản hồi từ OpenAI không chứa lựa chọn nào");
        }

        Object firstChoice = choices.get(0);
        if (!(firstChoice instanceof Map<?, ?> choiceMap)) {
            throw new RuntimeException("Định dạng lựa chọn trong phản hồi từ OpenAI không hợp lệ");
        }

        Object messageObject = choiceMap.get("message");
        if (!(messageObject instanceof Map<?, ?> messageMap)) {
            throw new RuntimeException("Định dạng tin nhắn trong phản hồi từ OpenAI không hợp lệ");
        }

        Object contentObject = messageMap.get("content");
        if (!(contentObject instanceof String content) || content.isBlank()) {
            throw new RuntimeException("Nội dung phản hồi từ OpenAI trống");
        }

        return content;
    }
}
