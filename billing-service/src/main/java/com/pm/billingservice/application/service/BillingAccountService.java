package com.pm.billingservice.application.service;


import com.pm.billingservice.domain.BillingAccount;
import com.pm.billingservice.infrastructure.exception.AppException;
import com.pm.billingservice.infrastructure.exception.ErrorCode;
import com.pm.billingservice.infrastructure.repo.BillingAccountRepository;
import com.pm.billingservice.infrastructure.repo.BillingTransactionRepository;
import com.pm.billingservice.interfaces.client.PatientClient;
import com.pm.billingservice.interfaces.dto.BillingAccountResponse;
import com.pm.billingservice.interfaces.dto.PatientResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingAccountService {
    private final BillingAccountRepository accountRepository;
    private final PatientClient patientClient;
    private final BillingTransactionRepository billingTransactionRepository;

    // Tạo BillingAccount
    @Transactional
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
    @Transactional
    public BillingAccountResponse getBillingAccount(UUID accountId) {

       BillingAccount bill =  accountRepository.findById(accountId)
               .orElseThrow(() -> new AppException(ErrorCode.BILLING_ACCOUNT_NOT_FOUND));
        PatientResponseDTO dto = patientClient.getPatientDetails(UUID.fromString(bill.getPatientId()));
        BillingAccountResponse res = BillingAccountResponse.builder()
                .id(bill.getId())
                .patientId(bill.getPatientId())
                .name(bill.getName())
                .email(bill.getEmail())
                .status(bill.getStatus())
                .createdAt(bill.getCreatedAt())
                .updatedAt(bill.getUpdatedAt())
                .balance(bill.getBalance())
                .currency(bill.getCurrency())
                .address(dto.getAddress())
                .dateOfBirth(dto.getDateOfBirth())
                .build();
        return res;
    }

    //  Cập nhật trạng thái account
    @Transactional
    public BillingAccount updateAccountStatus(UUID accountId, String status) {
        BillingAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setStatus(status);
        account.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }
    // delete account
    public void  deleteAccount(UUID accountId) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.BILLING_ACCOUNT_NOT_FOUND));
        accountRepository.deleteById(accountId);

    }
    @Transactional
    public BillingAccount deleteByPatientId(String patientId) {
        Optional<BillingAccount> account = accountRepository.findByPatientId(patientId);

        if (account.isEmpty()) {
            return null;
        }
        accountRepository.delete(account.get());
        return account.get();
    }


}
