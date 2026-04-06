package com.pm.appointmentservice.interfaces.client;

import com.pm.appointmentservice.interfaces.dto.DoctorResponseDTO;
import com.pm.appointmentservice.interfaces.dto.DoctorScheduleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "doctor-service", url = "http://doctor-service:4003")
public interface DoctorClient {
    @GetMapping("/doctors/{id}")
    DoctorResponseDTO getDoctorById(@PathVariable("id") UUID doctorId);

    @GetMapping("/doctors/{id}/schedules")
    List<DoctorScheduleDTO> getDoctorSchedules(@PathVariable("id") UUID doctorId,
                                               @RequestParam("date") LocalDate date);
}