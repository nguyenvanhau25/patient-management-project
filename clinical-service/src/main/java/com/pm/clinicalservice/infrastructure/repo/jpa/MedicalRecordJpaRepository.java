package com.pm.clinicalservice.infrastructure.repo.jpa;

import com.pm.clinicalservice.domain.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MedicalRecordJpaRepository extends JpaRepository<MedicalRecord, UUID> {
    List<MedicalRecord> findByPatientId(UUID patientId);
}
