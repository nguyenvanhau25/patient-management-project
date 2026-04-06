package com.pm.billingservice.domain.model.transaction;

import com.pm.billingservice.domain.Status;
import com.pm.billingservice.domain.model.account.BillingAccount;
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
    public static BillingTransaction create(
            BillingAccount account,
            Double amount,
            String type,
            String description
    ) {
        BillingTransaction tx = new BillingTransaction();
        tx.billingAccount = account;
        tx.amount = amount;
        tx.type = type;
        tx.description = description;
        tx.status = Status.PENDING;
        tx.createdAt = LocalDateTime.now();
        return tx;
    }

    public void markCompleted() {
        this.status = Status.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void markFailed() {
        this.status = Status.FAILED;
        this.completedAt = LocalDateTime.now();
    }
}
