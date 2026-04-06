package com.pm.clinicalservice.application.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class MedicalRecordResponseDTO {
    private String id;
    private String patientId;
    private String doctorId;
    private LocalDate visitDate;
    private String diagnosis;
    private String symptoms;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
