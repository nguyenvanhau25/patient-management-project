package com.pm.patientservice.domain.repository;

import com.pm.patientservice.domain.model.Patient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientDomainRepository {
    Patient save(Patient patient);

    Optional<Patient> findById(UUID id);

    List<Patient> findAll();

    boolean existsByEmail(String email);

    boolean existsByEmailAndNotId(String email, UUID id);

    boolean existsById(UUID id);
    void deleteById(UUID id);
}
