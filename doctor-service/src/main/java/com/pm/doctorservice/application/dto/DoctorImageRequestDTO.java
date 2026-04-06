package com.pm.doctorservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorImageRequestDTO {
    @NotBlank(message = "Image URL is required")
    private String profileImageUrl;
}
