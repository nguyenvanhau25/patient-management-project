package com.pm.appointmentservice.infrastructure.repo;

import com.pm.appointmentservice.domain.model.Appointment;
import com.pm.appointmentservice.domain.repository.AppointmentRepository;
import com.pm.appointmentservice.infrastructure.repo.jpa.AppointmentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AppointmentJpaAdapter implements AppointmentRepository {
    private final AppointmentJpaRepository appointmentJpaRepository;
    @Override
    public Appointment save(Appointment appointment) {
        return appointmentJpaRepository.save(appointment);
    }

    @Override
    public Optional<Appointment> findById(UUID id) {
        return appointmentJpaRepository.findById(id);
    }
}
