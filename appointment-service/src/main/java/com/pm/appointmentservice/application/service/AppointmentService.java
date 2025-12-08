package com.pm.appointmentservice.application.service;

import com.pm.appointmentservice.application.dto.AppointmentRequest;
import com.pm.appointmentservice.application.dto.AppointmentResponse;
import com.pm.appointmentservice.domain.Appointment;
import com.pm.appointmentservice.domain.AppointmentStatus;
import com.pm.appointmentservice.infrastructure.repo.AppointmentRepository;
import com.pm.appointmentservice.interfaces.client.PatientClient;
import com.pm.appointmentservice.interfaces.dto.PatientResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientClient patientClient;

    public Appointment addAppointment(AppointmentRequest appointmentRequest) {
        boolean exits = patientClient.checkPatientExits(appointmentRequest.getPatientId());
        if (!exits) {
            throw new RuntimeException("Patient not found");
        }
        Appointment appointment = Appointment.builder()
                .patientId(appointmentRequest.getPatientId())
                .appointmentTime(LocalDateTime.parse(appointmentRequest.getAppointmentDate()))
                .status(AppointmentStatus.SCHEDULED) // lên lịch chờ duyệt
                .build();
        return appointmentRepository.save(appointment);
    }

    public Appointment cancelAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.CANCELLED); // hủy lịch
        return appointmentRepository.save(appointment);
    }

    // xem thông tin bệnh nhân đặt lịch
    public AppointmentResponse getAppointment(UUID appointmentId) {
        // lấy ra thông tin lịch hẹn
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        // tìm kiếm patient
        PatientResponseDTO patient = patientClient.getPatientDetails(appointment.getPatientId());
        AppointmentResponse res = AppointmentResponse.builder()
                .name(patient.getName())
                .email(patient.getEmail())
                .dateOfBirth(patient.getDateOfBirth())
                .address(patient.getAddress())
                .appointmentDate(appointment.getAppointmentTime().toString())
                .status(appointment.getStatus())
                .build();
        return res;
    }

    // xác nhận lịch hẹn
    public boolean confirmAppointment(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new RuntimeException("Only scheduled appointments are allowed");
        }
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appointment);
        return true;
    }

    // từ chối lịch hẹn
    public boolean rejectAppointment(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new RuntimeException("Only scheduled appointments are allowed");
        }

        // không từ chối với lịch đã diễn ra
        if (appointment.getAppointmentTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot reject past appointments");
        }
        appointment.setStatus(AppointmentStatus.REJECTED);
        appointmentRepository.save(appointment);
        return true;
    }

    // Thay đổi giờ hẹn
    public void rescheduleAppointment(UUID id, LocalDateTime newDateTime) {
        Appointment ap = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Chỉ reschedule các lịch CONFIRMED hoặc SCHEDULE
        if (ap.getStatus() == AppointmentStatus.REJECTED
        ||ap.getStatus() == AppointmentStatus.CANCELLED) {
            throw new RuntimeException("Cannot reschedule appointment");
        }

        if (newDateTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("New appointment time must be in the future");
        }

        LocalDateTime oldDateTime = ap.getAppointmentTime();
        ap.setAppointmentTime(newDateTime);
        appointmentRepository.save(ap);

        log.info("Appointment [{}] rescheduled from {} to {}", id, oldDateTime, newDateTime);


    }
}