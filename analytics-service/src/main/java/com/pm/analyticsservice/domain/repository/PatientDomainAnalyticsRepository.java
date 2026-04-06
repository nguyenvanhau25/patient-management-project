package com.pm.analyticsservice.domain.repository;

import com.pm.analyticsservice.domain.model.PatientAnalytics;

import java.time.LocalDate;
import java.util.List;

public interface PatientDomainAnalyticsRepository {
    void save(PatientAnalytics patient);

    long countByDate(LocalDate date);

    List<PatientAnalytics> findByDate(LocalDate date);
}
