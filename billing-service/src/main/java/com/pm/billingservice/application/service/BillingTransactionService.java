package com.pm.billingservice.application.service;

import com.pm.billingservice.application.dto.BillingEventDto;
import com.pm.billingservice.domain.BillingAccount;
import com.pm.billingservice.domain.BillingTransaction;
import com.pm.billingservice.domain.Status;
import com.pm.billingservice.infrastructure.exception.AppException;
import com.pm.billingservice.infrastructure.exception.ErrorCode;
import com.pm.billingservice.infrastructure.kafka.KafkaProducer;
import com.pm.billingservice.infrastructure.repo.BillingAccountRepository;
import com.pm.billingservice.infrastructure.repo.BillingTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BillingTransactionService {
    private final BillingTransactionRepository transactionRepository;
    private final BillingAccountRepository accountRepository;
   private final KafkaProducer kafkaProducer;


    // Tạo transaction (charge/payment/refund)
    public BillingTransaction createTransaction(UUID accountId, Double amount, String type, String description) {
        BillingAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BillingTransaction transaction = new BillingTransaction();
        transaction.setBillingAccount(account);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setStatus(Status.PENDING);
        transaction.setDescription(description);
        transaction.setCreatedAt(LocalDateTime.now());

        // Lưu transaction
        return transactionRepository.save(transaction);
    }

    // Lấy lịch sử transaction của account
    public List<BillingTransaction> getTransactions(UUID accountId) {
        return transactionRepository.findByBillingAccountId(accountId);
    }


    // update status
    public BillingTransaction updateTransactionStatus(UUID transactionId, String status) {

        BillingTransaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_NOT_FOUND));

        transaction.setStatus(Status.valueOf(status));
        transaction.setCompletedAt(LocalDateTime.now());

        if (Status.COMPLETED.name().equals(status)) {

            BillingAccount account = transaction.getBillingAccount();

            if ("PAYMENT".equals(transaction.getType())) {
                account.setBalance(account.getBalance() + transaction.getAmount());
            }
            if ("CHARGE".equals(transaction.getType())) {
                account.setBalance(account.getBalance() - transaction.getAmount());
            }

        BillingEventDto dto = BillingEventDto.builder()
                .transactionId(transaction.getId().toString())
                .billingAccountId(account.getId().toString())
                .patientId(account.getPatientId())
                .amount(transaction.getAmount())
                .status(transaction.getStatus().name())
                .type(transaction.getType())
                .createdAt(transaction.getCreatedAt().toString())
                .build();
            accountRepository.save(account);
           kafkaProducer.sendEvent(dto);
        }

        return transactionRepository.save(transaction);
    }

}
