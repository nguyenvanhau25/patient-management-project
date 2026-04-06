package com.pm.appointmentservice.application.dto;

import com.pm.appointmentservice.domain.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Builder
@Schema(description = "Thông tin phản hồi của lịch hẹn")
public class AppointmentResponse {
    @Schema(description = "Tên bệnh nhân")
    private String name;
    @Schema(description = "Email bệnh nhân")
    private String email;
    @Schema(description = "Địa chỉ bệnh nhân")
    private String address;
    @Schema(description = "Ngày sinh bệnh nhân")
    private String dateOfBirth;
    @Schema(description = "ID bác sĩ")
    private UUID doctorId;
    @Schema(description = "Ngày hẹn")
    private String appointmentDate;
    @Schema(description = "Giờ bắt đầu")
    private String startTime;
    @Schema(description = "Giờ kết thúc")
    private String endTime;
    @Schema(description = "Trạng thái lịch hẹn")
    private AppointmentStatus status;
}
