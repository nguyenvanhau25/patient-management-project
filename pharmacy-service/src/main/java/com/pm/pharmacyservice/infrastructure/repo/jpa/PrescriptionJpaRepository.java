package com.pm.pharmacyservice.infrastructure.repo.jpa;

import com.pm.pharmacyservice.domain.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PrescriptionJpaRepository extends JpaRepository<Prescription, UUID> {
    List<Prescription> findByPatientId(UUID patientId);
}
