package com.pm.appointmentservice.domain.model;

import com.pm.appointmentservice.domain.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID patientId;
    private UUID doctorId;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    public static Appointment schedule(UUID patientId, UUID doctorId, LocalDate appointmentDate,
                                       LocalTime startTime, LocalTime endTime) {
        if (appointmentDate == null || startTime == null || endTime == null) {
            throw new IllegalArgumentException("Ngày và giờ hẹn phải được cung cấp");
        }

        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("Giờ kết thúc phải sau giờ bắt đầu");
        }

        LocalDateTime startDateTime = LocalDateTime.of(appointmentDate, startTime);
        if (startDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Thời gian hẹn phải ở hiện tại hoặc tương lai");
        }

        return Appointment.builder()
                .patientId(patientId)
                .doctorId(doctorId)
                .appointmentDate(appointmentDate)
                .startTime(startTime)
                .endTime(endTime)
                .status(AppointmentStatus.SCHEDULED)
                .build();
    }

    public void confirm() {
        if (status != AppointmentStatus.SCHEDULED) {
            throw new IllegalStateException("Chỉ những cuộc hẹn đã lên lịch mới có thể được xác nhận");
        }
        this.status = AppointmentStatus.CONFIRMED;
    }

    public void cancel() {
        if (status == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Lịch hẹn đã bị hủy rồi");
        }
        this.status = AppointmentStatus.CANCELLED;
    }

    public void reject() {
        if (status != AppointmentStatus.SCHEDULED) {
            throw new IllegalStateException("Chỉ những cuộc hẹn đã lên lịch mới có thể bị từ chối");
        }
        if (LocalDateTime.of(appointmentDate, startTime).isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Không thể từ chối các cuộc hẹn trong quá khứ");
        }
        this.status = AppointmentStatus.REJECTED;
    }

    public void reschedule(LocalDate newDate, LocalTime newStartTime, LocalTime newEndTime) {
        if (status == AppointmentStatus.CANCELLED || status == AppointmentStatus.REJECTED) {
            throw new IllegalStateException("Không thể đổi lịch hẹn đã hủy hoặc từ chối");
        }
        if (!newEndTime.isAfter(newStartTime)) {
            throw new IllegalArgumentException("Giờ kết thúc phải sau giờ bắt đầu");
        }
        LocalDateTime nextStart = LocalDateTime.of(newDate, newStartTime);
        if (nextStart.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Thời gian hẹn mới phải ở tương lai");
        }
        this.appointmentDate = newDate;
        this.startTime = newStartTime;
        this.endTime = newEndTime;
    }
}
