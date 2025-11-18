package com.pm.billingservice.infrastructure.repo;

import com.pm.billingservice.domain.BillingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BillingTransactionRepository extends JpaRepository<BillingTransaction, UUID> {
    List<BillingTransaction> findByBillingAccountId(UUID id);
}
