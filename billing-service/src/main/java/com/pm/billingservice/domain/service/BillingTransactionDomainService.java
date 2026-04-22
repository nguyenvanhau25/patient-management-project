package com.pm.billingservice.domain.service;

import com.pm.billingservice.domain.Status;
import com.pm.billingservice.domain.model.account.BillingAccount;
import com.pm.billingservice.domain.model.transaction.BillingTransaction;
import org.springframework.stereotype.Service;


@Service
public class BillingTransactionDomainService {
    public void completeTransaction(
            BillingAccount account,
            BillingTransaction transaction
    ) {

        if (transaction.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Giao dịch đã được xử lý");
        }

        if ("PAYMENT".equals(transaction.getType())) {
            account.increaseBalance(transaction.getAmount());
        }

        if ("CHARGE".equals(transaction.getType())) {
            account.decreaseBalance(transaction.getAmount());
        }

        transaction.markCompleted();
    }
    public void failTransaction(BillingTransaction tx) {
        tx.markFailed();
    }
    public BillingTransaction createTransaction(
            BillingAccount account,
            Double amount,
            String type,
            String description
    ) {
        BillingTransaction tx = BillingTransaction.create(
                account, amount, type, description
        );
        account.getTransactions().add(tx);
        return tx;
    }
}
