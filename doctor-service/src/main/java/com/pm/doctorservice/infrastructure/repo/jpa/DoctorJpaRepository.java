package com.pm.doctorservice.infrastructure.repo.jpa;

import com.pm.doctorservice.domain.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DoctorJpaRepository extends JpaRepository<Doctor, UUID> {
}
