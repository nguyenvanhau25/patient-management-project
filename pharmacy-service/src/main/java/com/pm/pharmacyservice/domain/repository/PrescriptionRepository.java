package com.pm.pharmacyservice.domain.repository;

import com.pm.pharmacyservice.domain.model.Prescription;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PrescriptionRepository {
    Prescription save(Prescription prescription);
    Optional<Prescription> findById(UUID id);
    List<Prescription> findByPatientId(UUID patientId);
}
