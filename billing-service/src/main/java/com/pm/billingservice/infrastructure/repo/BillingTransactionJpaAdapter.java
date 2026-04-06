package com.pm.billingservice.infrastructure.repo;

import com.pm.billingservice.domain.model.transaction.BillingTransaction;
import com.pm.billingservice.domain.repository.BillingTransactionDomainRepository;
import com.pm.billingservice.infrastructure.repo.jpa.BillingTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BillingTransactionJpaAdapter implements BillingTransactionDomainRepository {
    final private BillingTransactionRepository billingTransactionRepository;

    @Override
    public BillingTransaction save(BillingTransaction tx) {
        return billingTransactionRepository.save(tx);
    }

    @Override
    public Optional<BillingTransaction> findById(UUID id) {
        return billingTransactionRepository.findById(id);
    }

    @Override
    public List<BillingTransaction> findByAccountId(UUID accountId) {
        return billingTransactionRepository.findByBillingAccountId(accountId);
    }
}
