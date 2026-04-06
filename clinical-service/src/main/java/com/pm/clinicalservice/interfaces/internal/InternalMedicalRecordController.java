package com.pm.clinicalservice.interfaces.internal;

import com.pm.clinicalservice.application.dto.MedicalRecordResponseDTO;
import com.pm.clinicalservice.application.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/internal/medical-record")
@RequiredArgsConstructor
public class InternalMedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    @GetMapping("/{id}")
    public boolean exists(@PathVariable("id") UUID id) {
        return medicalRecordService.existsById(id);
    }

    @GetMapping("/detail/{id}")
    public MedicalRecordResponseDTO detail(@PathVariable("id") UUID id) {
        return medicalRecordService.getById(id);
    }
}
