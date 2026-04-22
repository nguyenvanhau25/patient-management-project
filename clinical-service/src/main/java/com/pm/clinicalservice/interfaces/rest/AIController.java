package com.pm.clinicalservice.interfaces.rest;

import com.pm.clinicalservice.application.dto.AIClinicalRequestDTO;
import com.pm.clinicalservice.application.dto.AIClinicalResponseDTO;
import com.pm.clinicalservice.application.service.AIClinicalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pm.clinicalservice.application.dto.ChatbotRequestDTO;
import com.pm.clinicalservice.application.dto.ChatbotResponseDTO;
import com.pm.clinicalservice.application.service.AIChatbotService;

@RestController
@RequestMapping("/ai-clinical")
@Tag(name = "Ho tro chan doan benh", description = "API ho tro tao mau chan doan dua tren du lieu benh an")
@RequiredArgsConstructor
@Validated
public class AIController {
    private final AIClinicalService aiClinicalService;
    private final AIChatbotService aiChatbotService;

    @PostMapping("/diagnosis-template")
    @Operation(summary = "Tao mau chan doan ho tro", description = "Lay thong tin benh nhan, lich su benh an va goi AI de tao mau chan doan cho bac si")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AIClinicalResponseDTO> generateDiagnosisTemplate(
            @Valid @RequestBody AIClinicalRequestDTO request) {
        return ResponseEntity.ok(aiClinicalService.generateDiagnosisTemplate(request));
    }

    @PostMapping("/chat")
    @Operation(summary = "Chatbot AI", description = "Giao tiếp với AI chatbot của bệnh viện")
    public ResponseEntity<ChatbotResponseDTO> chatWithAI(
            @Valid @RequestBody ChatbotRequestDTO request) {
        return ResponseEntity.ok(aiChatbotService.getChatbotResponse(request));
    }
}
