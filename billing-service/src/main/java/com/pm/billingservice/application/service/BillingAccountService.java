package com.pm.billingservice.application.service;


import com.pm.billingservice.domain.model.account.BillingAccount;
import com.pm.billingservice.domain.repository.BillingAccountDomainRepository;
import com.pm.billingservice.infrastructure.exception.AppException;
import com.pm.billingservice.infrastructure.exception.ErrorCode;
import com.pm.billingservice.interfaces.client.PatientClient;
import com.pm.billingservice.interfaces.dto.BillingAccountResponse;
import com.pm.billingservice.interfaces.dto.PatientResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BillingAccountService {
    private final PatientClient patientClient;
    private final BillingAccountDomainRepository  accountRepo;


    // Tạo BillingAccount
    public BillingAccount createBillingAccount(String patientId, String name, String email) {
        BillingAccount account = BillingAccount.create(patientId, name, email);
        return accountRepo.save(account);
    }

    //  Lấy thông tin account
    public BillingAccountResponse getBillingAccount(UUID accountId) {
        BillingAccount account = accountRepo.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.BILLING_ACCOUNT_NOT_FOUND));
        return mapToResponse(account);
    }

    public BillingAccountResponse getAccountByPatientId(String patientId) {
        BillingAccount account = accountRepo.findByPatientId(patientId)
                .orElseThrow(() -> new AppException(ErrorCode.BILLING_ACCOUNT_NOT_FOUND));
        return mapToResponse(account);
    }

    private BillingAccountResponse mapToResponse(BillingAccount account) {
        PatientResponseDTO dto = patientClient.getPatientDetails(UUID.fromString(account.getPatientId()));
        return BillingAccountResponse.builder()
                .id(account.getId())
                .patientId(account.getPatientId())
                .name(account.getName())
                .email(account.getEmail())
                .status(account.getStatus())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .address(dto.getAddress())
                .dateOfBirth(dto.getDateOfBirth())
                .build();
    }

    // APPLICATION chỉ gọi domain
    public BillingAccount updateAccountStatus(UUID accountId, String status) {
        BillingAccount account = accountRepo.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.BILLING_ACCOUNT_NOT_FOUND));

        account.changeStatus(status);

        return accountRepo.save(account);
    }

    // delete account
    public void  deleteAccount(UUID accountId) {
        BillingAccount account = accountRepo.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.BILLING_ACCOUNT_NOT_FOUND));
        accountRepo.delete(account);

    }

    public BillingAccount  deleteByPatientId(String patientId) {
        BillingAccount account = accountRepo.findByPatientId(patientId)
                .orElseThrow(() -> new AppException(ErrorCode.BILLING_ACCOUNT_NOT_FOUND));

       return accountRepo.delete(account);
    }


    // nạp tiền vào tài khoản
    public BillingAccount recharge(UUID accountId, Double amount) {
        BillingAccount account = accountRepo.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.BILLING_ACCOUNT_NOT_FOUND));

        account.recharge(amount);
        return accountRepo.save(account);
    }



}
