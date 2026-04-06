package com.pm.pharmacyservice.interfaces.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "clinical-service", url = "http://clinical-service:4007")
public interface ClinicalClient {
    @GetMapping("/internal/medical-record/{id}")
    boolean checkMedicalRecordExists(@PathVariable("id") UUID id);
}
