package com.pm.appointmentservice.application.service;

import com.pm.appointmentservice.application.dto.AppointmentRequest;
import com.pm.appointmentservice.application.dto.AppointmentResponse;
import com.pm.appointmentservice.domain.model.Appointment;
import com.pm.appointmentservice.domain.AppointmentStatus;
import com.pm.appointmentservice.domain.repository.AppointmentRepository;
import com.pm.appointmentservice.interfaces.client.DoctorClient;
import com.pm.appointmentservice.interfaces.client.PatientClient;
import com.pm.appointmentservice.interfaces.dto.DoctorScheduleDTO;
import com.pm.appointmentservice.interfaces.dto.DoctorResponseDTO;
import com.pm.appointmentservice.interfaces.dto.PatientResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.NullArgumentException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientClient patientClient;
    private final DoctorClient doctorClient;

    public Appointment addAppointment(AppointmentRequest req) {

        if (req.getDoctorId() == null || req.getPatientId() == null) {
            throw new IllegalArgumentException("ID bệnh nhân và ID bác sĩ là bắt buộc");
        }

        if (!patientClient.checkPatientExits(req.getPatientId())) {
            throw new RuntimeException("Không tìm thấy thông tin bệnh nhân");
        }

        DoctorResponseDTO doctor = doctorClient.getDoctorById(req.getDoctorId());
        if (doctor == null) {
            throw new RuntimeException("Không tìm thấy thông tin bác sĩ");
        }

        LocalDate date;
        LocalTime startTime;
        LocalTime endTime;
        try {
            date = LocalDate.parse(req.getAppointmentDate());
            startTime = LocalTime.parse(req.getStartTime());
            endTime = LocalTime.parse(req.getEndTime());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Định dạng ngày/giờ không hợp lệ. Sử dụng yyyy-MM-dd và HH:mm");
        }

        List<DoctorScheduleDTO> availableSchedules = doctorClient.getDoctorSchedules(req.getDoctorId(), date);
        boolean hasSlot = availableSchedules.stream()
                .anyMatch(s -> Boolean.TRUE.equals(s.getIsAvailable())
                        && !s.getStartTime().isAfter(startTime)
                        && !s.getEndTime().isBefore(endTime));

        if (!hasSlot) {
            throw new RuntimeException("Bác sĩ không có lịch trống vào thời gian yêu cầu");
        }

        Appointment appointment = Appointment.schedule(
                req.getPatientId(),
                req.getDoctorId(),
                date,
                startTime,
                endTime
        );

        return appointmentRepository.save(appointment);
    }

    public Appointment cancelAppointment(UUID id) {
        Appointment ap = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));
        ap.cancel();
        return appointmentRepository.save(ap);
    }

    // xác nhận lịch hẹn
    public boolean confirmAppointment(UUID id) {
        Appointment ap = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));
        ap.confirm();
        appointmentRepository.save(ap);
        return true;
    }


    // từ chối lịch hẹn
    public boolean rejectAppointment(UUID id) {
        Appointment ap = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));
        ap.reject();
        appointmentRepository.save(ap);
        return true;
    }
    // xem thông tin bệnh nhân đặt lịch
    public AppointmentResponse getAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));
        // tìm kiếm patient
       PatientResponseDTO patient = patientClient.getPatientDetails(appointment.getPatientId());
        AppointmentResponse res = AppointmentResponse.builder()
                .name(patient.getName())
                .email(patient.getEmail())
                .dateOfBirth(patient.getDateOfBirth())
                .address(patient.getAddress())
                .doctorId(appointment.getDoctorId())
                .appointmentDate(appointment.getAppointmentDate().toString())
                .startTime(appointment.getStartTime().toString())
                .endTime(appointment.getEndTime().toString())
                .status(appointment.getStatus())
                .build();
        return res;
    }



    public void rescheduleAppointment(UUID id, LocalDate newDate, LocalTime newStartTime, LocalTime newEndTime) {
        Appointment ap = appointmentRepository.findById(id).orElse(null);
        if (ap == null) {
            throw new RuntimeException("Không tìm thấy lịch hẹn");
        }
        ap.reschedule(newDate, newStartTime, newEndTime);
        appointmentRepository.save(ap);

        log.info("Appointment [{}] rescheduled to {} {}-{}", id, newDate, newStartTime, newEndTime);
    }

}