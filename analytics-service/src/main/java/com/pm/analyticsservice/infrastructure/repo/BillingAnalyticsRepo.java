package com.pm.analyticsservice.infrastructure.repo;

import com.pm.analyticsservice.domain.BillingAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface BillingAnalyticsRepo extends JpaRepository<BillingAnalytics, UUID> {


    List<BillingAnalytics> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    List<BillingAnalytics> findByPatientId(String patientId);

    List<BillingAnalytics> findByStatus(String status);
}
