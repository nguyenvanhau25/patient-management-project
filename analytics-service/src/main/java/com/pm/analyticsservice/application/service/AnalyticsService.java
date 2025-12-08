package com.pm.analyticsservice.application.service;

import com.pm.analyticsservice.domain.PatientAnalytics;
import com.pm.analyticsservice.infrastructure.repo.BillingAnalyticsRepo;
import com.pm.analyticsservice.infrastructure.repo.PatientAnalyticsRepository;
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
    private final PatientAnalyticsRepository repository;
    private final BillingAnalyticsRepo billingRepo;

    // Lưu event nhận được từ Kafka vào DB
    public void savePatientEvent(PatientEvent event) {
        PatientAnalytics pa = new PatientAnalytics();
        pa.setPatientId(event.getPatientId());
        pa.setName(event.getName());
        pa.setEmail(event.getEmail());
        pa.setCreatedDate(LocalDate.now());
        repository.save(pa);
    }

    // Lấy tổng số bệnh nhân tạo mới trong ngày
    public long countPatientsByDate(LocalDate date) {
        return repository.countByCreatedDate(date);
    }

    // Lấy danh sách tất cả bệnh nhân tạo mới trong ngày
    public List<PatientAnalytics> getPatientsByDate(LocalDate date) {
        return repository.findByCreatedDate(date);
    }

    //tăng trưởng bệnh nhân theo ngày
    public double trackDailyGrowthRate() {

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        long todayCount = repository.countByCreatedDate(today);
        long yesterdayCount = repository.countByCreatedDate(yesterday);

        if (yesterdayCount == 0) {
            if (todayCount == 0) return 0;   // không có gì
            return 100.0;                    // tăng trưởng 100%
        }

        double rate = ((double)(todayCount - yesterdayCount) / yesterdayCount) * 100;

        return Math.round(rate * 100.0) / 100.0; // làm tròn 2 chữ số thập phân
    }
}
