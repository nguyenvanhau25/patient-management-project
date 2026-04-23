package com.pm.clinicalservice.interfaces.rest;

import com.pm.clinicalservice.application.dto.ChatbotRequestDTO;
import com.pm.clinicalservice.application.dto.ChatbotResponseDTO;
import com.pm.clinicalservice.application.service.AIChatbotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai-clinical")
@Tag(name = "AI Assistant", description = "API cho AI chatbot của bệnh viện")
@RequiredArgsConstructor
@Validated
public class AIController {
    private final AIChatbotService aiChatbotService;

    @PostMapping("/chat")
    @Operation(summary = "Chatbot AI", description = "Giao tiếp với AI chatbot của bệnh viện")
    public ResponseEntity<ChatbotResponseDTO> chatWithAI(
            @Valid @RequestBody ChatbotRequestDTO request) {
        return ResponseEntity.ok(aiChatbotService.getChatbotResponse(request));
    }
}
