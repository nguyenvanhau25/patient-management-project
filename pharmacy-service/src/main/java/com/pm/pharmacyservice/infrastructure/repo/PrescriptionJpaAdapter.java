package com.pm.pharmacyservice.infrastructure.repo;

import com.pm.pharmacyservice.domain.model.Prescription;
import com.pm.pharmacyservice.domain.repository.PrescriptionRepository;
import com.pm.pharmacyservice.infrastructure.repo.jpa.PrescriptionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PrescriptionJpaAdapter implements PrescriptionRepository {
    private final PrescriptionJpaRepository jpaRepository;

    @Override
    public Prescription save(Prescription prescription) {
        return jpaRepository.save(prescription);
    }

    @Override
    public Optional<Prescription> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Prescription> findByPatientId(UUID patientId) {
        return jpaRepository.findByPatientId(patientId);
    }
}
