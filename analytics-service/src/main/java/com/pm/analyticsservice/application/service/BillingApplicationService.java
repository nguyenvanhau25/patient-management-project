package com.pm.analyticsservice.application.service;

import billing.events.BillingEvent;
import com.pm.analyticsservice.application.interfaces.BillingService;
import com.pm.analyticsservice.domain.model.BillingAnalytics;
import com.pm.analyticsservice.domain.repository.BillingDomainAnalyticsRepository;
import com.pm.analyticsservice.domain.service.BillingDomainService;
import com.pm.analyticsservice.infrastructure.repo.jpa.BillingAnalyticsJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingApplicationService implements BillingService {
    private final BillingDomainAnalyticsRepository repository;
    private final BillingDomainService domainService;

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

        repository.save(analytics);
    }
// tổng doanh thu theo ngày
    @Override
    public Double getTotalRevenue(LocalDate date) {
        List<BillingAnalytics> transactions =
                repository.findByDate(date);

        return domainService.calculateTotalRevenue(transactions);
    }
// top doanh thu theo bệnh nhân
    @Override
    public List<Object[]> getTopPatientSpending(int limit) {

        Map<String, Double> revenueByPatient =
                domainService.calculateRevenueByPatient(
                        repository.findCompleted()
                );

        return revenueByPatient.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(limit)
                .map(e -> new Object[]{e.getKey(), e.getValue()})
                .toList();
    }
// tổng giao dịch thành công
    @Override
    public Long countCompletedTransactions() {
        return (long) repository.findCompleted().size();
    }
// doanh thu theo bệnh nhân
    @Override
    public Double getRevenueByPatientId(String patientId) {
        return domainService.calculateTotalRevenue(
                repository.findByPatientId(patientId)
        );
    }
}
