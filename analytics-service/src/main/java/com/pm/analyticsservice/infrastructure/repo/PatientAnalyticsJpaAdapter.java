package com.pm.analyticsservice.infrastructure.repo;

import com.pm.analyticsservice.domain.model.PatientAnalytics;
import com.pm.analyticsservice.domain.repository.PatientDomainAnalyticsRepository;
import com.pm.analyticsservice.infrastructure.repo.jpa.PatientAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PatientAnalyticsJpaAdapter implements PatientDomainAnalyticsRepository {
    private final PatientAnalyticsRepository jpaRepo;
    @Override
    public void save(PatientAnalytics patient) {
        jpaRepo.save(patient);
    }

    @Override
    public long countByDate(LocalDate date) {
        return jpaRepo.countByCreatedDate(date);
    }

    @Override
    public List<PatientAnalytics> findByDate(LocalDate date) {
        return jpaRepo.findByCreatedDate(date);
    }
}
