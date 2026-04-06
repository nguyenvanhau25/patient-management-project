package com.pm.billingservice.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillingEventDto {

    @NotBlank(message = "Transaction ID is required")
    private String transactionId;

    @NotBlank(message = "Billing account ID is required")
    private String billingAccountId;

    @NotBlank(message = "Patient ID is required")
    private String patientId;

    @NotBlank(message = "Type is required")
    private String type;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private Double amount;

    @NotBlank(message = "Status is required")
    private String status;

    private String createdAt;
}
