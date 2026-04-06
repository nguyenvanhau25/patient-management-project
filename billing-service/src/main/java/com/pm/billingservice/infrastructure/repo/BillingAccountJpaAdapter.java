package com.pm.billingservice.infrastructure.repo;

import com.pm.billingservice.domain.model.account.BillingAccount;
import com.pm.billingservice.domain.repository.BillingAccountDomainRepository;
import com.pm.billingservice.infrastructure.repo.jpa.BillingAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class BillingAccountJpaAdapter implements BillingAccountDomainRepository {
    private final BillingAccountRepository jpaRepo;

    @Override
    public BillingAccount save(BillingAccount account) {
        return jpaRepo.save(account);
    }

    @Override
    public Optional<BillingAccount> findById(UUID id) {
        return jpaRepo.findById(id);
    }

    @Override
    public Optional<BillingAccount> findByPatientId(String patientId) {
        return jpaRepo.findByPatientId(patientId);
    }

    @Override
    public BillingAccount  delete(BillingAccount account) {
         jpaRepo.delete(account);
         if(jpaRepo.findById(account.getId()).isPresent()){
             return account;
         }
         return null;
    }
}
