package com.pm.analyticsservice.domain.service;

import com.pm.analyticsservice.domain.model.BillingAnalytics;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BillingDomainService {
    public double calculateTotalRevenue(List<BillingAnalytics> transactions) {
        return transactions.stream()
                .mapToDouble(BillingAnalytics::revenueIfCompleted)
                .sum();
    }

    public Map<String, Double> calculateRevenueByPatient(List<BillingAnalytics> transactions) {
        return transactions.stream()
                .filter(BillingAnalytics::isCompleted)
                .collect(Collectors.groupingBy(
                        BillingAnalytics::getPatientId,
                        Collectors.summingDouble(BillingAnalytics::getAmount)
                ));
    }
}
