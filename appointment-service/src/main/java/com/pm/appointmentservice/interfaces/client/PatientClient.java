package com.pm.appointmentservice.interfaces.client;

import com.pm.appointmentservice.interfaces.dto.PatientResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "patient-service")
public interface PatientClient {
    @GetMapping("/internal/patient/{id}")
    boolean checkPatientExits(@PathVariable("id") UUID patientId);

    @GetMapping("/internal/patient/detail/{id}")
    PatientResponseDTO getPatientDetails(@PathVariable("id") UUID patientId);
}
