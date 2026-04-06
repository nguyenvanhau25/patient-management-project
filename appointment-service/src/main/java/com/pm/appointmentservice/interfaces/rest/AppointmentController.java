package com.pm.appointmentservice.interfaces.rest;

import com.pm.appointmentservice.application.dto.AppointmentRequest;
import com.pm.appointmentservice.application.dto.AppointmentResponse;
import com.pm.appointmentservice.application.service.AppointmentService;
import com.pm.appointmentservice.domain.model.Appointment;
import com.pm.appointmentservice.infrastructure.exception.ApiResponse;
import com.pm.appointmentservice.infrastructure.exception.AppException;
import com.pm.appointmentservice.infrastructure.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@RestController
@RequestMapping("/appointment")
@Tag(name = "Lịch hẹn", description = "API quản lý lịch hẹn của bệnh nhân")
@RequiredArgsConstructor
@Validated
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Tạo lịch hẹn")
    public ResponseEntity<ApiResponse<Appointment>> addAppointment(@RequestBody @Valid AppointmentRequest appointment) {
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
    @Operation(summary = "Hủy lịch hẹn")
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
                .result("Xác nhận lịch hẹn thành công")
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
                .result("Đã từ chối lịch hẹn")
                .build());
    }

    @PostMapping("/{id}/reschedule")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Đổi lịch hẹn")
    public ResponseEntity<ApiResponse<String>> reschedule(
            @PathVariable UUID id,
            @RequestParam String appointmentDate,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        LocalDate newDate;
        LocalTime newStart;
        LocalTime newEnd;
        try {
            newDate = LocalDate.parse(appointmentDate);
            newStart = LocalTime.parse(startTime);
            newEnd = LocalTime.parse(endTime);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_DATE_FORMAT);
        }

        appointmentService.rescheduleAppointment(id, newDate, newStart, newEnd);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code("SUCCESS")
                .message("Đổi lịch hẹn thành công")
                .result("Lịch hẹn đã được dời lịch thành công")
                .build());
    }
}
