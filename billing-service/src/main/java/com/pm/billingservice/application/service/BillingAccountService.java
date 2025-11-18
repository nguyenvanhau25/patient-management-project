package com.pm.billingservice.application.service;

import com.pm.billingservice.domain.BillingAccount;
import com.pm.billingservice.domain.BillingTransaction;
import com.pm.billingservice.infrastructure.repo.BillingAccountRepository;
import com.pm.billingservice.infrastructure.repo.BillingTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BillingAccountService {
    private final BillingAccountRepository accountRepository;
    private final BillingTransactionRepository transactionRepository;

    // Tạo BillingAccount
    public BillingAccount createBillingAccount(String patientId, String name, String email) {
        BillingAccount account = new BillingAccount();
        account.setPatientId(patientId);
        account.setName(name);
        account.setEmail(email);
        account.setStatus("ACTIVE");
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    //  Lấy thông tin account
    public Optional<BillingAccount> getBillingAccount(UUID accountId) {
        return accountRepository.findById(accountId);
    }

    //  Cập nhật trạng thái account
    public BillingAccount updateAccountStatus(UUID accountId, String status) {
        BillingAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setStatus(status);
        account.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }
    // delete account
    public void deleteAccount(UUID accountId) {
        accountRepository.deleteById(accountId);
    }
    public BillingAccount deleteByPatientId(String patientId) {
        Optional<BillingAccount> account = accountRepository.findByPatientId(patientId);

        if (account.isEmpty()) {
            return null;
        }

        accountRepository.delete(account.get());
        return account.get();
    }

    // Tạo transaction (charge/payment/refund)
    public BillingTransaction createTransaction(UUID accountId, Double amount, String type, String description) {
        BillingAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BillingTransaction transaction = new BillingTransaction();
        transaction.setBillingAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setStatus("PENDING");
        transaction.setDescription(description);
        transaction.setCreatedAt(LocalDateTime.now());

        // Lưu transaction
        return transactionRepository.save(transaction);
    }

    // Lấy lịch sử transaction của account
    public List<BillingTransaction> getTransactions(UUID accountId) {
        return transactionRepository.findByBillingAccountId(accountId);
    }

    // 6ập nhật trạng thái transaction
    public BillingTransaction updateTransactionStatus(UUID transactionId, String status) {
        BillingTransaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        transaction.setStatus(status);
        transaction.setCompletedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }
}
