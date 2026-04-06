package com.pm.patientservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientImageRequestDTO {
    @NotBlank(message = "Image URL is required")
    private String profileImageUrl;
}
