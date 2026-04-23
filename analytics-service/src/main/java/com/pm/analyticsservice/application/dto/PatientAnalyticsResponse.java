package com.pm.analyticsservice.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@Builder
public class PatientAnalyticsResponse {
    private UUID id;
    private String patientId;
    private String name;
    private String email;
    private LocalDate createdDate;
}
