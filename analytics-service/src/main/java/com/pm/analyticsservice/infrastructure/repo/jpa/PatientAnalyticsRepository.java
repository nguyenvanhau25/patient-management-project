package com.pm.analyticsservice.infrastructure.repo.jpa;

import com.pm.analyticsservice.domain.model.PatientAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PatientAnalyticsRepository extends JpaRepository<PatientAnalytics, UUID> {
    List<PatientAnalytics> findByCreatedDate(LocalDate date);

    long countByCreatedDate(LocalDate date);
}
