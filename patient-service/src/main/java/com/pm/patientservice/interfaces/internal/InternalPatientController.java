package com.pm.patientservice.interfaces.internal;

import com.pm.patientservice.application.dto.PatientResponseDTO;
import com.pm.patientservice.application.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/patient")
@RequiredArgsConstructor
public class InternalPatientController {
    private final PatientService patientService;

    @GetMapping("/{id}")
    public boolean checkPatientExists(@PathVariable("id") UUID id) {

        return patientService.existsById(id);
    }

    @GetMapping("/detail/{id}")
    public PatientResponseDTO getDetailPatient(@PathVariable UUID id) {
        return patientService.getPatientById(id);
    }
}
