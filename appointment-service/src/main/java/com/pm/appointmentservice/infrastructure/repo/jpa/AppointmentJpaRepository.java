package com.pm.appointmentservice.infrastructure.repo.jpa;

import com.pm.appointmentservice.domain.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppointmentJpaRepository extends JpaRepository<Appointment, UUID> {
}
