package com.pm.pharmacyservice.application.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PrescriptionResponseDTO {
    private String id;
    private String patientId;
    private String doctorId;
    private String medicalRecordId;
    private List<PrescriptionItemDTO> items;
    private LocalDateTime createdAt;

    // miêu tả chi tiết đơn thuốc
    @Getter
    @Setter
    public static class PrescriptionItemDTO {
        private String medicineId;
        private Integer quantity;
        private String dosage;
        private String instructions;
    }
}
