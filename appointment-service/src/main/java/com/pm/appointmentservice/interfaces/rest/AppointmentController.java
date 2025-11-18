package com.pm.appointmentservice.interfaces.rest;

import com.pm.appointmentservice.application.dto.AppointmentRequest;
import com.pm.appointmentservice.application.dto.AppointmentResponse;
import com.pm.appointmentservice.application.service.AppointmentService;
import com.pm.appointmentservice.domain.Appointment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/appointment")
@Tag(name = "appointment ", description = "api for appointment of patient")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    @Operation(summary = "tao lich hen")
    public ResponseEntity<Appointment> addAppointment(@RequestBody AppointmentRequest appointment) {
        Appointment appointments = appointmentService.addAppointment(appointment);

        return new ResponseEntity<>(appointments, HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/{id}")
    @Operation(summary = "huy hen")
    public ResponseEntity<Appointment> cancelAppointment(@PathVariable UUID id) {
        Appointment appointment = appointmentService.cancelAppointment(id);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{appointmentId}")
    @Operation(summary = "xem thông tin bệnh nhân từ id lịch hẹn")
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable UUID appointmentId) {
        return new ResponseEntity<>(appointmentService.getAppointment(appointmentId), HttpStatus.OK);
    }

}
