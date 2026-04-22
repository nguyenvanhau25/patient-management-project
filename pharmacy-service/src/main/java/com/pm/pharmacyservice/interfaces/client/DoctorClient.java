package com.pm.pharmacyservice.interfaces.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "doctor-service", url = "${services.doctor.url}")
public interface DoctorClient {
    @GetMapping("/internal/doctor/{id}")
    boolean checkDoctorExists(@PathVariable("id") UUID id);
}
