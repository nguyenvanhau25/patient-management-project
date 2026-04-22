package com.pm.clinicalservice.interfaces.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientSummaryDTO {
    private String id;
    private String name;
    private String email;
    private String address;
    private String dateOfBirth;
    private String profileImageUrl;
}
