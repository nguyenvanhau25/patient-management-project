package com.pm.clinicalservice.interfaces.client;

import com.pm.clinicalservice.interfaces.client.dto.DoctorSummaryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "doctor-service", url = "http://doctor-service:4003")
public interface DoctorClient {
    @GetMapping("/internal/doctor/{id}")
    boolean checkDoctorExists(@PathVariable("id") UUID doctorId);

    @GetMapping("/internal/doctor/detail/{id}")
    DoctorSummaryDTO getDoctorDetail(@PathVariable("id") UUID doctorId);
}
