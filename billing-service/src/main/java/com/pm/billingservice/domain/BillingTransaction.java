package com.pm.billingservice.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class BillingTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "billing_account_id", nullable = false)
    private BillingAccount billingAccount;

    private String type;            // CHARGE, PAYMENT, REFUND
    private Double amount;

    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String description;     //mô tả chi tiết giao dịch
    @Enumerated(EnumType.STRING)
    private Status status;          // PENDING, COMPLETED, FAILED
}
