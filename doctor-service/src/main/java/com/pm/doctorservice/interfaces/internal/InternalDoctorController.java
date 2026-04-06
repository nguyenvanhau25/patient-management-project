package com.pm.doctorservice.interfaces.internal;

import com.pm.doctorservice.application.dto.DoctorResponseDTO;
import com.pm.doctorservice.application.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/internal/doctor")
@RequiredArgsConstructor
public class InternalDoctorController {
    private final DoctorService doctorService;

    @GetMapping("/{id}")
    public boolean existsById(@PathVariable("id") UUID id) {
        return doctorService.existsById(id);
    }

    @GetMapping("/detail/{id}")
    public DoctorResponseDTO getById(@PathVariable("id") UUID id) {
        return doctorService.getDoctorById(id);
    }
}
