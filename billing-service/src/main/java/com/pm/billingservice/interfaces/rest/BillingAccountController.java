package com.pm.billingservice.interfaces.rest;

import com.pm.billingservice.application.service.BillingAccountService;
import com.pm.billingservice.domain.BillingAccount;
import com.pm.billingservice.domain.BillingTransaction;
import com.pm.billingservice.interfaces.dto.CreateBillingAccountRequest;
import com.pm.billingservice.interfaces.dto.CreateTransactionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/billing")
@RequiredArgsConstructor
@Tag(name = "Billing API", description = "API quản lý billing account và transaction")
public class BillingAccountController {
    private final BillingAccountService billingAccountService;


    // 1. Tạo BillingAccount (ADMIN)
    @PostMapping("/accounts")
    @Operation(summary = "Tạo tài khoản Billing mới")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BillingAccount>
    createBillingAccount(@RequestBody CreateBillingAccountRequest request) {

        BillingAccount account = billingAccountService.createBillingAccount(
                request.getPatientId(),
                request.getName(),
                request.getEmail()
        );
        return ResponseEntity.ok(account);
    }

    // 2. Lấy thông tin BillingAccount theo ID (USER + ADMIN)
    @GetMapping("/accounts/{accountId}")
    @Operation(summary = "Lấy thông tin BillingAccount theo ID")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<BillingAccount> getBillingAccount(@PathVariable UUID accountId) {
        return billingAccountService.getBillingAccount(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Cập nhật trạng thái Account (ADMIN)
    @PatchMapping("/accounts/{accountId}/status")
    @Operation(summary = "Cập nhật trạng thái BillingAccount")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BillingAccount>
    updateAccountStatus(@PathVariable UUID accountId,
                        @RequestParam String status) {
        BillingAccount account = billingAccountService.updateAccountStatus(accountId, status);
        return ResponseEntity.ok(account);
    }

    // 4. Xóa account (ADMIN)
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa BillingAccount")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBillingAccount(@PathVariable UUID id) {
        billingAccountService.deleteAccount(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 5. Tạo transaction (USER + ADMIN)
    @PostMapping("/accounts/{accountId}/transactions")
    @Operation(summary = "Tạo giao dịch cho Billing Account")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<BillingTransaction> createTransaction(@PathVariable UUID accountId,
                                                                @RequestBody CreateTransactionRequest request) {
        BillingTransaction transaction = billingAccountService.createTransaction(
                accountId,
                request.getAmount(),
                request.getType(),
                request.getDescription()
        );
        return ResponseEntity.ok(transaction);
    }

    // 6. Lấy danh sách transaction (USER + ADMIN)
    @GetMapping("/accounts/{accountId}/transactions")
    @Operation(summary = "Lấy danh sách transaction theo account ID")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<BillingTransaction>>
    getTransactions(@PathVariable UUID accountId) {
        List<BillingTransaction> transactions =
                billingAccountService.getTransactions(accountId);
        return ResponseEntity.ok(transactions);
    }

    // 7. Update trạng thái transaction (ADMIN)
    @PatchMapping("/transactions/{transactionId}/status")
    @Operation(summary = "Cập nhật trạng thái giao dịch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BillingTransaction>
    updateTransactionStatus(@PathVariable UUID transactionId,
                            @RequestParam String status) {
        BillingTransaction transaction = billingAccountService
                .updateTransactionStatus(transactionId, status);
        return ResponseEntity.ok(transaction);
    }


}
