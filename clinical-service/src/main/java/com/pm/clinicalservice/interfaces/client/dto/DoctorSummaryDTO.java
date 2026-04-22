package com.pm.clinicalservice.interfaces.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorSummaryDTO {
    private String id;
    private String name;
    private String email;
    private String specialization;
    private String phoneNumber;
    private Integer experienceYears;
    private String profileImageUrl;
}
