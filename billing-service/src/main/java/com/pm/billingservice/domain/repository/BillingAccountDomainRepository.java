package com.pm.billingservice.domain.repository;

import com.pm.billingservice.domain.model.account.BillingAccount;

import java.util.Optional;
import java.util.UUID;


public interface BillingAccountDomainRepository {
    BillingAccount save(BillingAccount account);

    Optional<BillingAccount> findById(UUID id);

    Optional<BillingAccount> findByPatientId(String patientId);

    BillingAccount delete(BillingAccount account);
}
