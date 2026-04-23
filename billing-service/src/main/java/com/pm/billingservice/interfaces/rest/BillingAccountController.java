package com.pm.billingservice.interfaces.rest;

import com.pm.billingservice.application.service.BillingAccountService;
import com.pm.billingservice.application.service.BillingTransactionService;
import com.pm.billingservice.domain.model.account.BillingAccount;
import com.pm.billingservice.domain.model.transaction.BillingTransaction;
import com.pm.billingservice.infrastructure.exception.ApiResponse;
import com.pm.billingservice.interfaces.dto.*;
import com.pm.billingservice.interfaces.mapper.BillingMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/billing")
@RequiredArgsConstructor
@Validated
@Tag(name = "Billing API", description = "API quản lý billing account và transaction")
public class BillingAccountController {
    private final BillingAccountService billingAccountService;
    private final BillingTransactionService billingTransactionService;

    // 1. Tạo BillingAccount (ADMIN)
    @PostMapping("/accounts")
    @Operation(summary = "Tạo tài khoản Billing mới")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<BillingAccountResponse>> createBillingAccount(@Valid @RequestBody CreateBillingAccountRequest request) {
        BillingAccount account = billingAccountService.createBillingAccount(
                request.getPatientId(),
                request.getName(),
                request.getEmail()
        );
        return ResponseEntity.ok(ApiResponse.success(BillingMapper.toAccountResponse(account)));
    }

    // 2. xem thông tin account
    @GetMapping("/accounts/{accountId}")
    @Operation(summary = "Lấy thông tin BillingAccount theo ID")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<BillingAccountResponse>> getBillingAccount(@PathVariable UUID accountId) {
        BillingAccountResponse response = billingAccountService.getBillingAccount(accountId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/accounts/patient/{patientId}")
    @Operation(summary = "Lấy thông tin BillingAccount theo patientId")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<BillingAccountResponse>> getAccountByPatientId(@PathVariable String patientId) {
        BillingAccountResponse response = billingAccountService.getAccountByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 3. Cập nhật trạng thái Account (ADMIN)
    @PatchMapping("/accounts/{accountId}/status")
    @Operation(summary = "Cập nhật trạng thái BillingAccount")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<BillingAccountResponse>> updateAccountStatus(
            @PathVariable UUID accountId,
            @RequestBody StatusUpdateRequest request) {
        BillingAccount account = billingAccountService.updateAccountStatus(accountId, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success(BillingMapper.toAccountResponse(account)));
    }

    // 4. Xóa account (ADMIN)
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa BillingAccount")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBillingAccount(@PathVariable UUID id) {
        billingAccountService.deleteAccount(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // Tạo transaction (USER + ADMIN)
    @PostMapping("/accounts/{accountId}/transactions")
    @Operation(summary = "Tạo giao dịch cho Billing Account")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<BillingTransactionResponse>> createTransaction(
            @PathVariable UUID accountId,
            @Valid @RequestBody CreateTransactionRequest request) {
        BillingTransaction transaction = billingTransactionService.createTransaction(
                accountId,
                request.getAmount(),
                request.getType(),
                request.getDescription()
        );
        return ResponseEntity.ok(ApiResponse.success(BillingMapper.toTransactionResponse(transaction)));
    }

    // Lấy lịch sử transaction (USER + ADMIN)
    @GetMapping("/accounts/{accountId}/transactions")
    @Operation(summary = "Lấy danh sách transaction theo account ID")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<BillingTransactionResponse>>> getTransactions(@PathVariable UUID accountId) {
        List<BillingTransaction> transactions = billingTransactionService.getTransactions(accountId);
        List<BillingTransactionResponse> response = transactions.stream()
                .map(BillingMapper::toTransactionResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    //Update trạng thái transaction (ADMIN)
    @PatchMapping("/transactions/{transactionId}/status")
    @Operation(summary = "Cập nhật trạng thái giao dịch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BillingTransactionResponse>> updateTransactionStatus(
            @PathVariable UUID transactionId,
            @RequestBody StatusUpdateRequest request) {
        BillingTransaction transaction = billingTransactionService.updateTransactionStatus(transactionId, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success(BillingMapper.toTransactionResponse(transaction)));
    }

    // nạp tiền vào tài khoản
    @PatchMapping("/accounts/{accountId}/recharge")
    @Operation(summary = "Nạp tiền vào tài khoản")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<BillingAccountResponse>> recharge(
            @PathVariable UUID accountId,
            @RequestBody RechargeRequest request) {
        BillingAccount billingAccount = billingAccountService.recharge(accountId, request.getAmount());
        return ResponseEntity.ok(ApiResponse.success(BillingMapper.toAccountResponse(billingAccount)));
    }

}
