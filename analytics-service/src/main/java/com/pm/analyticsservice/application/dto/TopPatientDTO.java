package com.pm.analyticsservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopPatientDTO {
    private String patientId;
    private Double totalSpent;
    private String patientName;
}
