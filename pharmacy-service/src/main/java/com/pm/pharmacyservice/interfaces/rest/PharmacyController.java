package com.pm.pharmacyservice.interfaces.rest;

import com.pm.pharmacyservice.application.dto.MedicineRequestDTO;
import com.pm.pharmacyservice.application.dto.MedicineResponseDTO;
import com.pm.pharmacyservice.application.dto.PrescriptionRequestDTO;
import com.pm.pharmacyservice.application.dto.PrescriptionResponseDTO;
import com.pm.pharmacyservice.application.service.PharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pharmacy")
@Tag(name = "Hiệu thuốc", description = "Các endpoint về hiệu thuốc và đơn thuốc")
@RequiredArgsConstructor
@Validated
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @GetMapping("/medicines")
    @Operation(summary = "Danh sách thuốc", description = "Lấy tất cả các loại thuốc hiện có")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<MedicineResponseDTO>> listMedicines() {
        return ResponseEntity.ok(pharmacyService.listMedicines());
    }

    @PostMapping("/medicines")
    @Operation(summary = "Thêm thuốc", description = "Thêm một bản ghi thuốc mới")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicineResponseDTO> addMedicine(@Valid @RequestBody MedicineRequestDTO medicineRequest) {
        return ResponseEntity.status(201).body(pharmacyService.addMedicine(medicineRequest));
    }

    @PostMapping("/medicines/{id}/upload-image")
    @Operation(summary = "Tải lên ảnh thuốc", description = "Tải lên tệp ảnh vật lý cho thuốc")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> uploadMedicineImage(
            @PathVariable UUID id,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        pharmacyService.uploadMedicineImage(id, file);
        return ResponseEntity.ok("Tải ảnh lên thành công");
    }

    @PostMapping("/prescriptions")
    @Operation(summary = "Tạo đơn thuốc", description = "Tạo đơn thuốc cho bệnh nhân")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PrescriptionResponseDTO> createPrescription(@Valid @RequestBody PrescriptionRequestDTO prescriptionRequest) {
        return ResponseEntity.status(201).body(pharmacyService.prescribe(prescriptionRequest));
    }

    @GetMapping("/prescriptions/patient/{patientId}")
    @Operation(summary = "Lấy đơn thuốc theo bệnh nhân", description = "Lấy tất cả đơn thuốc của một bệnh nhân")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<PrescriptionResponseDTO>> getPrescriptionByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(pharmacyService.getPrescriptionsByPatient(patientId));
    }

    @PostMapping("/prescriptions/{id}/dispense")
    @Operation(summary = "Cấp phát thuốc", description = "Cấp phát thuốc theo đơn và cập nhật kho")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PrescriptionResponseDTO> dispensePrescription(@PathVariable UUID id) {
        return ResponseEntity.ok(pharmacyService.dispense(id));
    }
}
