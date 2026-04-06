package com.pm.analyticsservice.infrastructure.repo;

import com.pm.analyticsservice.domain.model.BillingAnalytics;
import com.pm.analyticsservice.domain.repository.BillingDomainAnalyticsRepository;
import com.pm.analyticsservice.infrastructure.repo.jpa.BillingAnalyticsJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BillingAnalyticsJpaAdapter implements BillingDomainAnalyticsRepository {

    private final BillingAnalyticsJpaRepository jpaRepo;

    @Override
    public void save(BillingAnalytics analytics) {
        jpaRepo.save(analytics);
    }

    @Override
    public List<BillingAnalytics> findByDate(LocalDate date) {
        var start = date.atStartOfDay();
        var end = date.atTime(23, 59, 59);

        return jpaRepo.findByCreatedAtBetween(start, end);
    }

    @Override
    public List<BillingAnalytics> findCompleted() {
        return jpaRepo.findByStatus("COMPLETED");
    }

    @Override
    public List<BillingAnalytics> findByPatientId(String patientId) {
        return jpaRepo.findByPatientId(patientId);
    }
}
