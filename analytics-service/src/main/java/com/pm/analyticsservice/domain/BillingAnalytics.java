package com.pm.analyticsservice.domain;

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

}

