package com.pm.analyticsservice.domain.repository;

import com.pm.analyticsservice.domain.model.BillingAnalytics;

import java.time.LocalDate;
import java.util.List;

public interface BillingDomainAnalyticsRepository {
    void save(BillingAnalytics analytics);

    List<BillingAnalytics> findByDate(LocalDate date);

    List<BillingAnalytics> findCompleted();

    List<BillingAnalytics> findByPatientId(String patientId);
}
