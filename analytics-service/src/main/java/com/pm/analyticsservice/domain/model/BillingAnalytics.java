package com.pm.analyticsservice.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class BillingAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String transactionId;
    private String billingAccountId;
    private String patientId;
    private String type;
    private double amount;
    private String status;
    private String eventType;
    private LocalDateTime createdAt;

    public boolean isCompleted() {
        return "COMPLETED".equals(this.status);
    }

    public double revenueIfCompleted() {
        return isCompleted() ? amount : 0.0;
    }
}

