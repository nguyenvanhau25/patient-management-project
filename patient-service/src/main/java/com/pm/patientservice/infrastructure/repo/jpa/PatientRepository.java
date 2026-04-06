package com.pm.patientservice.infrastructure.repo.jpa;

import com.pm.patientservice.domain.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface PatientRepository extends JpaRepository<Patient, UUID> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, UUID id);
}
