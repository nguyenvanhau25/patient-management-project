package com.pm.analyticsservice.application.service;

import com.pm.analyticsservice.domain.model.PatientAnalytics;
import com.pm.analyticsservice.domain.repository.PatientDomainAnalyticsRepository;
import com.pm.analyticsservice.domain.service.PatientDomainService;
import com.pm.analyticsservice.infrastructure.repo.jpa.BillingAnalyticsJpaRepository;
import com.pm.analyticsservice.infrastructure.repo.jpa.PatientAnalyticsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AnalyticsService {
    private final PatientDomainAnalyticsRepository patientRepo;
    private final PatientDomainService growthService;

    // Lưu event nhận được từ Kafka vào DB
    public void savePatientEvent(PatientEvent event) {
        PatientAnalytics patient = PatientAnalytics.fromEvent(
                event.getPatientId(),
                event.getName(),
                event.getEmail()
        );

        patientRepo.save(patient);
    }

    // Lấy tổng số bệnh nhân tạo mới trong ngày
    public long countPatientsByDate(LocalDate date) {
        return patientRepo.countByDate(date);
    }

    // Lấy danh sách tất cả bệnh nhân tạo mới trong ngày
    public List<PatientAnalytics> getPatientsByDate(LocalDate date) {
        return patientRepo.findByDate(date);
    }

    //tăng trưởng bệnh nhân theo ngày
    public double trackDailyGrowthRate() {

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        long todayCount = patientRepo.countByDate(today);
        long yesterdayCount = patientRepo.countByDate(yesterday);

        return growthService.calculateGrowthRate(todayCount, yesterdayCount);
    }
}
