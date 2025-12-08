package com.pm.analyticsservice.application.service;

import billing.events.BillingEvent;
import com.pm.analyticsservice.application.interfaces.BillingService;
import com.pm.analyticsservice.domain.BillingAnalytics;
import com.pm.analyticsservice.infrastructure.repo.BillingAnalyticsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {
    private final BillingAnalyticsRepo billingAnalyticsRepository;

    @Override
    public void saveBillingEvent(BillingEvent event) {
        BillingAnalytics analytics = new BillingAnalytics();

        analytics.setTransactionId(event.getTransactionId());
        analytics.setBillingAccountId(event.getBillingAccountId());
        analytics.setPatientId(event.getPatientId());
        analytics.setType(event.getType());
        analytics.setAmount(event.getAmount());
        analytics.setStatus(event.getStatus());
        analytics.setEventType(event.getEventType());
        analytics.setCreatedAt(LocalDateTime.parse(event.getCreatedAt()));

        billingAnalyticsRepository.save(analytics);
    }
// tổng doanh thu theo ngày
    @Override
    public Double getTotalRevenue(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        return billingAnalyticsRepository
                .findByCreatedAtBetween(start, end)
                .stream()
                .filter(tx -> "COMPLETED".equals(tx.getStatus()))
                .mapToDouble(BillingAnalytics::getAmount)
                .sum();
    }
// top doanh thu theo bệnh nhân
    @Override
    public List<Object[]> getTopPatientSpending(int limit) {

        return billingAnalyticsRepository
                .findByStatus("COMPLETED")
                .stream()
                .collect(Collectors.groupingBy(
                        BillingAnalytics::getPatientId,
                        Collectors.summingDouble(BillingAnalytics::getAmount)
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(limit)
                .map(e -> new Object[]{ e.getKey(), e.getValue() })
                .toList();
    }
// tổng giao dịch thành công
    @Override
    public Long countCompletedTransactions() {
        return (long) billingAnalyticsRepository
                .findByStatus("COMPLETED")
                .size();
    }
// doanh thu theo bệnh nhân
    @Override
    public Double getRevenueByPatientId(String patientId) {
        return billingAnalyticsRepository
                .findByPatientId(patientId)
                .stream()
                .filter(tx -> "COMPLETED".equals(tx.getStatus()))
                .mapToDouble(BillingAnalytics::getAmount)
                .sum();
    }
}
