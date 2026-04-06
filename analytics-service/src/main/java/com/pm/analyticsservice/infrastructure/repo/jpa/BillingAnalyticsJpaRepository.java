package com.pm.analyticsservice.infrastructure.repo.jpa;

import com.pm.analyticsservice.domain.model.BillingAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface BillingAnalyticsJpaRepository extends JpaRepository<BillingAnalytics, UUID> {


    List<BillingAnalytics> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    List<BillingAnalytics> findByPatientId(String patientId);

    List<BillingAnalytics> findByStatus(String status);
}
