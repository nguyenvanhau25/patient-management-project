package com.pm.analyticsservice.application.mapper;

import com.pm.analyticsservice.application.dto.PatientAnalyticsResponse;
import com.pm.analyticsservice.domain.model.PatientAnalytics;

public class AnalyticsMapper {
    public static PatientAnalyticsResponse toResponse(PatientAnalytics analytics) {
        if (analytics == null) return null;
        return PatientAnalyticsResponse.builder()
                .id(analytics.getId())
                .patientId(analytics.getPatientId())
                .name(analytics.getName())
                .email(analytics.getEmail())
                .createdDate(analytics.getCreatedDate())
                .build();
    }
}
