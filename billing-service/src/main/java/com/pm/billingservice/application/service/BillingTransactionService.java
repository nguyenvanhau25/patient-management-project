package com.pm.billingservice.application.service;

import com.pm.billingservice.application.dto.BillingEventDto;
import com.pm.billingservice.domain.model.account.BillingAccount;
import com.pm.billingservice.domain.model.transaction.BillingTransaction;
import com.pm.billingservice.domain.Status;
import com.pm.billingservice.domain.repository.BillingAccountDomainRepository;
import com.pm.billingservice.domain.repository.BillingTransactionDomainRepository;
import com.pm.billingservice.domain.service.BillingTransactionDomainService;
import com.pm.billingservice.infrastructure.exception.AppException;
import com.pm.billingservice.infrastructure.exception.ErrorCode;
import com.pm.billingservice.infrastructure.kafka.KafkaProducer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BillingTransactionService {
    private final KafkaProducer kafkaProducer;
    private final BillingAccountDomainRepository accountRepo;
    private final BillingTransactionDomainRepository transactionRepo;
    private final BillingTransactionDomainService domainService;


    // Tạo transaction (charge/payment/refund)
    public BillingTransaction createTransaction(
            UUID accountId,
            Double amount,
            String type,
            String description
    ) {
        BillingAccount account = accountRepo.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.BILLING_ACCOUNT_NOT_FOUND));
        return domainService.createTransaction(account,amount, type, description);
    }


    // Lấy lịch sử transaction của account
    public List<BillingTransaction> getTransactions(UUID accountId) {
        return transactionRepo.findByAccountId(accountId);
    }
    // complete giao dịch


    // update status
    public BillingTransaction updateTransactionStatus(UUID transactionId, String status) {

        BillingTransaction transaction = transactionRepo.findById(transactionId)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_NOT_FOUND));

        if (Status.COMPLETED.name().equals(status)) {

            BillingAccount account = transaction.getBillingAccount();

            domainService.completeTransaction(account, transaction);

            accountRepo.save(account);
            BillingEventDto dto = BillingEventDto.builder()
                    .transactionId(transaction.getId().toString())
                    .billingAccountId(account.getId().toString())
                    .patientId(account.getPatientId())
                    .amount(transaction.getAmount())
                    .status(transaction.getStatus().name())
                    .type(transaction.getType())
                    .createdAt(transaction.getCreatedAt().toString())
                    .build();
            kafkaProducer.sendEvent(dto);

        }
        return transaction;

    }
}
