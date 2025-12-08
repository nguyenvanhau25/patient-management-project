package com.pm.appointmentservice.interfaces.rest;

import com.pm.appointmentservice.application.dto.AppointmentRequest;
import com.pm.appointmentservice.application.dto.AppointmentResponse;
import com.pm.appointmentservice.application.service.AppointmentService;
import com.pm.appointmentservice.domain.Appointment;
import com.pm.appointmentservice.infrastructure.exception.ApiResponse;
import com.pm.appointmentservice.infrastructure.exception.AppException;
import com.pm.appointmentservice.infrastructure.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/appointment")
@Tag(name = "appointment ", description = "api for appointment of patient")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "tao lich hen")
    public ResponseEntity<ApiResponse<Appointment>> addAppointment(@RequestBody AppointmentRequest appointment) {
        Appointment appt = appointmentService.addAppointment(appointment);
        return ResponseEntity.status(201)
                .body(ApiResponse.<Appointment>builder()
                        .code("SUCCESS")
                        .message("Tạo lịch hẹn thành công")
                        .result(appt)
                        .build());
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "huy hen")
    public ResponseEntity<ApiResponse<Appointment>> cancelAppointment(@PathVariable UUID id) {
        Appointment appt = appointmentService.cancelAppointment(id);
        if (appt == null) {
            throw new AppException(ErrorCode.APPOINTMENT_NOT_FOUND);
        }
        return ResponseEntity.ok(ApiResponse.<Appointment>builder()
                .code("SUCCESS")
                .message("Hủy lịch hẹn thành công")
                .result(appt)
                .build());
    }

    @GetMapping("/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "xem thông tin bệnh nhân từ id lịch hẹn")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointment(@PathVariable UUID appointmentId) {
        AppointmentResponse response = appointmentService.getAppointment(appointmentId);

        if (response == null) {
            throw new AppException(ErrorCode.APPOINTMENT_NOT_FOUND, "Không tìm thấy lịch hẹn với id: " + appointmentId);
        }

        return ResponseEntity.ok(ApiResponse.<AppointmentResponse>builder()
                .code("SUCCESS")
                .message("Lấy thông tin lịch hẹn thành công")
                .result(response)
                .build());
    }


    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "xác nhận lịch hẹn")
    public ResponseEntity<ApiResponse<String>> confirmAppointment(@PathVariable UUID id) {
        boolean success = appointmentService.confirmAppointment(id);

        if (!success) {
            throw new AppException(ErrorCode.APPOINTMENT_NOT_FOUND, "Không tìm thấy lịch hẹn để xác nhận với id: " + id);
        }

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code("SUCCESS")
                .message("Xác nhận lịch hẹn thành công")
                .result("Appointment confirmed successfully")
                .build());
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "từ chối lịch hẹn")
    public ResponseEntity<ApiResponse<String>> rejectAppointment(@PathVariable UUID id) {
        boolean success = appointmentService.rejectAppointment(id);

        if (!success) {
            throw new AppException(ErrorCode.APPOINTMENT_NOT_FOUND, "Không tìm thấy lịch hẹn để từ chối với id: " + id);
        }

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code("SUCCESS")
                .message("Từ chối lịch hẹn thành công")
                .result("Appointment rejected")
                .build());
    }

    @PostMapping("/{id}/reschedule")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "đổi lịch hẹn")
    public ResponseEntity<ApiResponse<String>> reschedule(
            @PathVariable UUID id,
            @RequestParam String newDateTime) {
        LocalDateTime newDt;
        try {
            newDt = LocalDateTime.parse(newDateTime);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_DATE_FORMAT);
        }

        appointmentService.rescheduleAppointment(id, newDt);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code("SUCCESS")
                .message("Reschedule thành công")
                .result("Appointment rescheduled successfully")
                .build());
    }
}
