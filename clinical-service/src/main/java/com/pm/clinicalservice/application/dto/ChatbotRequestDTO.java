package com.pm.clinicalservice.application.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ChatbotRequestDTO {
    @NotEmpty
    private String message;
    
    private List<ChatMessageDTO> history;
}
