package com.pm.analyticsservice.domain.service;

import org.springframework.stereotype.Service;

@Service
public class PatientDomainService {
    public double calculateGrowthRate(long today, long yesterday) {

        if (yesterday == 0) {
            return today == 0 ? 0 : 100.0;
        }

        double rate = ((double) (today - yesterday) / yesterday) * 100;
        return Math.round(rate * 100.0) / 100.0;
    }
}
