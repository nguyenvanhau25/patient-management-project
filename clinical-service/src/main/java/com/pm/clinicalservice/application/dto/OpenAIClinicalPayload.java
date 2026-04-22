package com.pm.clinicalservice.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OpenAIClinicalPayload {
    private String clinicalSummary;
    private String suggestedDiagnosis;
    private String riskLevel;
    private List<String> recommendedActions;
    private List<String> redFlags;
}
