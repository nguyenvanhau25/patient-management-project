package com.pm.billingservice.domain.model.account;

import com.pm.billingservice.domain.Status;
import com.pm.billingservice.domain.model.transaction.BillingTransaction;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Setter
@Getter
public class BillingAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String patientId;
    private String name;
    private String email;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double balance = 0.0;    // số dư tài khoản
    private String currency = "VND"; // đơn vị tiền tệ


    @OneToMany(mappedBy = "billingAccount", cascade = CascadeType.ALL)
    private List<BillingTransaction> transactions = new ArrayList<>();


    public static BillingAccount create(String patientId, String name, String email) {
        BillingAccount account = new BillingAccount();
        account.patientId = patientId;
        account.name = name;
        account.email = email;
        account.status = "ACTIVE";
        account.balance = 0.0;
        account.currency = "VND";
        account.createdAt = LocalDateTime.now();
        account.updatedAt = LocalDateTime.now();
        return account;
    }

    public void changeStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void recharge(Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Số tiền nạp phải là số dương");
        }
        this.balance += amount;
        this.updatedAt = LocalDateTime.now();
    }


    public void increaseBalance(Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Số tiền phải là số dương");
        }
        this.balance += amount;
        this.updatedAt = LocalDateTime.now();
    }

    public void decreaseBalance(Double amount) {
        if (this.balance < amount) {
            throw new IllegalStateException("Số dư không đủ");
        }
        this.balance -= amount;
        this.updatedAt = LocalDateTime.now();
    }

}
