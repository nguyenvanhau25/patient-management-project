package com.pm.appointmentservice.interfaces.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DoctorResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String specialization;
    private String phoneNumber;
    private Integer experienceYears;
}
