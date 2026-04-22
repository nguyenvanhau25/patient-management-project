package com.pm.clinicalservice.infrastructure.repo;

import com.pm.clinicalservice.domain.model.MedicalRecord;
import com.pm.clinicalservice.domain.repository.MedicalRecordRepository;
import com.pm.clinicalservice.infrastructure.repo.jpa.MedicalRecordJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MedicalRecordJpaAdapter implements MedicalRecordRepository {
    private final MedicalRecordJpaRepository jpaRepository;

    @Override
    public MedicalRecord save(MedicalRecord medicalRecord) {
        return jpaRepository.save(medicalRecord);
    }

    @Override
    public Optional<MedicalRecord> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<MedicalRecord> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<MedicalRecord> findByPatientId(UUID patientId) {
        return jpaRepository.findByPatientId(patientId);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }
}
