package com.pm.clinicalservice.interfaces.rest;

import com.pm.clinicalservice.application.dto.MedicalRecordRequestDTO;
import com.pm.clinicalservice.application.dto.MedicalRecordResponseDTO;
import com.pm.clinicalservice.application.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/medical-records")
@Tag(name = "Hồ sơ y tế", description = "Các endpoint quản lý hồ sơ bệnh án")
@RequiredArgsConstructor
@Validated
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    @Operation(summary = "Tạo hồ sơ bệnh án", description = "Bác sĩ tạo một hồ sơ bệnh án mới cho bệnh nhân")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicalRecordResponseDTO> createMedicalRecord(@Valid @RequestBody MedicalRecordRequestDTO request) {
        return ResponseEntity.status(201).body(medicalRecordService.create(request));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Lấy hồ sơ bệnh án theo bệnh nhân", description = "Lấy tất cả hồ sơ bệnh án của một bệnh nhân")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<MedicalRecordResponseDTO>> getByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(medicalRecordService.getByPatientId(patientId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy hồ sơ bệnh án theo ID", description = "Lấy thông tin chi tiết của một hồ sơ bệnh án")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MedicalRecordResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(medicalRecordService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật hồ sơ bệnh án", description = "Cập nhật thông tin hồ sơ bệnh án theo ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicalRecordResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody MedicalRecordRequestDTO payload) {
        return ResponseEntity.ok(medicalRecordService.update(id, payload));
    }
}
