package com.pm.clinicalservice.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AIClinicalResponseDTO {
    private String patientId;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private String specialty;
    private String clinicalSummary;
    private String suggestedDiagnosis;
    private String riskLevel;
    private List<String> recommendedActions;
    private List<String> redFlags;
    private String disclaimer;
    private Integer historicalRecordCount;
    private boolean aiGenerated;
}
