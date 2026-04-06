package com.pm.billingservice.domain.repository;

import com.pm.billingservice.domain.model.transaction.BillingTransaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BillingTransactionDomainRepository {
    BillingTransaction save(BillingTransaction tx);
    Optional<BillingTransaction> findById(UUID id);
    List<BillingTransaction> findByAccountId(UUID accountId);
}
