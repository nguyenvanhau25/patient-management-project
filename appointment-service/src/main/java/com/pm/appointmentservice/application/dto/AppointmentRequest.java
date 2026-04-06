package com.pm.appointmentservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.util.UUID;

@Data
@Schema(description = "Thông tin yêu cầu đặt lịch hẹn")
public class AppointmentRequest {
    @NotNull(message = "ID bệnh nhân là bắt buộc")
    @Schema(description = "ID của bệnh nhân", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID patientId;

    @NotNull(message = "ID bác sĩ là bắt buộc")
    @Schema(description = "ID của bác sĩ", example = "123e4567-e89b-12d3-a456-426614174001")
    private UUID doctorId;

    @NotBlank(message = "Ngày hẹn là bắt buộc")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Ngày phải ở định dạng yyyy-MM-dd")
    @Schema(description = "Ngày hẹn (định dạng yyyy-MM-dd)", example = "2024-04-10")
    private String appointmentDate; // yyyy-MM-dd

    @NotBlank(message = "Giờ bắt đầu là bắt buộc")
    @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "Giờ bắt đầu phải ở định dạng HH:mm")
    @Schema(description = "Giờ bắt đầu (định dạng HH:mm)", example = "09:00")
    private String startTime; // HH:mm

    @NotBlank(message = "Giờ kết thúc là bắt buộc")
    @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "Giờ kết thúc phải ở định dạng HH:mm")
    @Schema(description = "Giờ kết thúc (định dạng HH:mm)", example = "10:00")
    private String endTime; // HH:mm
}
