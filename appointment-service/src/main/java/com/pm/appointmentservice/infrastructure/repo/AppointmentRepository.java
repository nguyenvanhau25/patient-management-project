package com.pm.appointmentservice.infrastructure.repo;

import com.pm.appointmentservice.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
}
