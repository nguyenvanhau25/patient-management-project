package com.pm.clinicalservice.domain.repository;

import com.pm.clinicalservice.domain.model.MedicalRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MedicalRecordRepository {
    MedicalRecord save(MedicalRecord medicalRecord);
    Optional<MedicalRecord> findById(UUID id);
    List<MedicalRecord> findAll();
    List<MedicalRecord> findByPatientId(UUID patientId);
    boolean existsById(UUID id);
}
