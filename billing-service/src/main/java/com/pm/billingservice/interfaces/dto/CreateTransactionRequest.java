package com.pm.billingservice.interfaces.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateTransactionRequest {
    private Double amount;
    private String type; // CHARGE, PAYMENT, REFUND
    private String description;
}
