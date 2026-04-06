package com.pm.billingservice.infrastructure.repo.jpa;

import com.pm.billingservice.domain.model.transaction.BillingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BillingTransactionRepository extends JpaRepository<BillingTransaction, UUID> {
    List<BillingTransaction> findByBillingAccountId(UUID accountId);

}
