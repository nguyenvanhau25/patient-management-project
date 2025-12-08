package com.pm.billingservice.application.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillingEventDto {

    private String transactionId;
    private String billingAccountId;
    private String patientId;
    private String type;
    private Double amount;
    private String status;
    private String createdAt;
}
