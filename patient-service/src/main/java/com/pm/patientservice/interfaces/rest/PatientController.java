package com.pm.patientservice.interfaces.rest;


import com.pm.patientservice.application.dto.PatientRequestDTO;
import com.pm.patientservice.application.dto.PatientResponseDTO;
import com.pm.patientservice.application.dto.PatientImageRequestDTO;
import com.pm.patientservice.application.dto.validators.CreatePatientValidationGroup;
import com.pm.patientservice.application.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name = "Bệnh nhân", description = "API quản lý thông tin bệnh nhân")
public class PatientController {

    private final PatientService patientService;


    @GetMapping
    @Operation(summary = "Lấy danh sách bệnh nhân")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        List<PatientResponseDTO> patients = patientService.getPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }


    @PostMapping
    @Operation(summary = "Tạo mới bệnh nhân")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Validated({Default.class, CreatePatientValidationGroup.class})
            @RequestBody PatientRequestDTO patientRequestDTO) {

        PatientResponseDTO patientResponseDTO = patientService.createPatient(
                patientRequestDTO);

        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin bệnh nhân")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id,
                                                            @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO) {

        PatientResponseDTO patientResponseDTO = patientService.updatePatient(id,
                patientRequestDTO);

        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa bệnh nhân")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
    // 5 xem thông tin bản thân bệnh nhân
    @GetMapping("/{id}")
    @Operation(summary = "xem thông tin bản thân bệnh nhân")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PatientResponseDTO> getPatient(@PathVariable UUID id) {
        return new ResponseEntity<>(patientService.getPatientById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}/image")
    @Operation(summary = "Cập nhật ảnh bệnh nhân (URL)")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PatientResponseDTO> updatePatientImage(
            @PathVariable UUID id,
            @RequestBody @Validated PatientImageRequestDTO requestDTO) {
        return ResponseEntity.ok(patientService.updatePatientImage(id, requestDTO.getProfileImageUrl()));
    }

    @PostMapping("/{id}/upload-image")
    @Operation(summary = "Tải lên ảnh bệnh nhân (Tệp vật lý)")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<String> uploadPatientImage(
            @PathVariable UUID id,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        patientService.uploadImage(id, file);
        return ResponseEntity.ok("Tải ảnh lên thành công");
    }

    @GetMapping("/pdf")
    @Operation(summary = "xuất báo cáo bệnh nhân")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public void exportPatientPdf(HttpServletResponse response) throws Exception {
        patientService.exportPatientPdf(response);
    }

}
