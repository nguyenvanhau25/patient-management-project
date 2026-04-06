package com.pm.doctorservice.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorResponseDTO {
    private String id;
    private String name;
    private String email;
    private String specialization;
    private String phoneNumber;
    private Integer experienceYears;
    private String profileImageUrl;
}
