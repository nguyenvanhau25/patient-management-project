package com.pm.pharmacyservice.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PrescriptionRequestDTO {
    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    @NotNull(message = "Medical record ID is required")
    private UUID medicalRecordId;

    @NotEmpty(message = "Prescription items cannot be empty")
    @Valid
    private List<PrescriptionItemDTO> items;

    @Getter
    @Setter
    public static class PrescriptionItemDTO {
        @NotNull(message = "Medicine ID is required")
        private UUID medicineId;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        @NotBlank(message = "Dosage is required")
        private String dosage;

        private String instructions;
    }
}
