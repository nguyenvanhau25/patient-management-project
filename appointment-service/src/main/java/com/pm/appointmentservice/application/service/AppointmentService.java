package com.pm.appointmentservice.application.service;

import com.pm.appointmentservice.application.dto.AppointmentRequest;
import com.pm.appointmentservice.application.dto.AppointmentResponse;
import com.pm.appointmentservice.domain.Appointment;
import com.pm.appointmentservice.domain.AppointmentStatus;
import com.pm.appointmentservice.infrastructure.AppointmentRepository;
import com.pm.appointmentservice.interfaces.client.PatientClient;
import com.pm.appointmentservice.interfaces.dto.PatientResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
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
                    .status(AppointmentStatus.SCHEDULED)
                    .build();
            return appointmentRepository.save(appointment);
    }
    public Appointment cancelAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.CANCELLED);
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
}
