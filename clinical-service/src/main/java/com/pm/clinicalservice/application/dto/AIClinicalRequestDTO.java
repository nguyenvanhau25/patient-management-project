package com.pm.clinicalservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AIClinicalRequestDTO {
    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    @NotBlank(message = "Chief complaint is required")
    @Size(max = 1000, message = "Chief complaint cannot exceed 1000 characters")
    private String chiefComplaint;

    @Size(max = 2000, message = "Current symptoms cannot exceed 2000 characters")
    private String currentSymptoms;

    @Size(max = 2000, message = "Clinical notes cannot exceed 2000 characters")
    private String notes;
}
