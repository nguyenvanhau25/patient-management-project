package com.pm.billingservice.interfaces.dto;

import com.pm.billingservice.domain.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
public class BillingTransactionResponse {
    private UUID id;
    private UUID billingAccountId;
    private String type;
    private Double amount;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String description;
    private Status status;
}
