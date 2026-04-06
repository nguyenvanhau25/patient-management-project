package com.pm.clinicalservice.interfaces.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "patient-service", url = "http://patient-service:4000")
public interface PatientClient {
    @GetMapping("/internal/patient/{id}")
    boolean checkPatientExists(@PathVariable("id") UUID patientId);
}
