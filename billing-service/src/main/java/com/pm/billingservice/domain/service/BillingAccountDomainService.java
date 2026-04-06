package com.pm.billingservice.domain.service;

import com.pm.billingservice.domain.model.account.BillingAccount;
import org.springframework.stereotype.Service;

@Service
public class BillingAccountDomainService {
    public BillingAccount createAccount(String patientId, String name, String email) {
        return BillingAccount.create(patientId, name, email);
    }

    public void recharge(BillingAccount account, Double amount) {
        account.recharge(amount);
    }
}
