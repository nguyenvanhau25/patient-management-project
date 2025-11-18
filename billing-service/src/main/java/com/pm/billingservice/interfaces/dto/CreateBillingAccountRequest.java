package com.pm.billingservice.interfaces.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateBillingAccountRequest {
    private String patientId;
    private String name;
    private String email;
}
