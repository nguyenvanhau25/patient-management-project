package com.pm.billingservice.interfaces.mapper;

import com.pm.billingservice.domain.model.account.BillingAccount;
import com.pm.billingservice.domain.model.transaction.BillingTransaction;
import com.pm.billingservice.interfaces.dto.BillingAccountResponse;
import com.pm.billingservice.interfaces.dto.BillingTransactionResponse;

public class BillingMapper {

    public static BillingAccountResponse toAccountResponse(BillingAccount account) {
        if (account == null) return null;
        return BillingAccountResponse.builder()
                .id(account.getId())
                .patientId(account.getPatientId())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .name(account.getName())
                .email(account.getEmail())
                .build();
    }

    public static BillingTransactionResponse toTransactionResponse(BillingTransaction tx) {
        if (tx == null) return null;
        return BillingTransactionResponse.builder()
                .id(tx.getId())
                .billingAccountId(tx.getBillingAccount().getId())
                .type(tx.getType())
                .amount(tx.getAmount())
                .createdAt(tx.getCreatedAt())
                .completedAt(tx.getCompletedAt())
                .description(tx.getDescription())
                .status(tx.getStatus())
                .build();
    }
}
