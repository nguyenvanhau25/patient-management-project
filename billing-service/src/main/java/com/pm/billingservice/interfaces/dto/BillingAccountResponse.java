package com.pm.billingservice.interfaces.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
public class BillingAccountResponse {
    private UUID id;

    private String patientId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double balance = 0.0;    // số dư tài khoản
    private String currency = "VND";

    private String name;
    private String email;
    private String address;
    private String dateOfBirth;
}
