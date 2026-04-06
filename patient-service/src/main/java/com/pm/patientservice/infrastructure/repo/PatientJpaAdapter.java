package com.pm.patientservice.infrastructure.repo;

import com.pm.patientservice.domain.model.Patient;
import com.pm.patientservice.domain.repository.PatientDomainRepository;
import com.pm.patientservice.infrastructure.repo.jpa.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PatientJpaAdapter implements PatientDomainRepository {

    private final PatientRepository jpaRepo;

    @Override
    public Patient save(Patient patient) {
        return jpaRepo.save(patient);
    }

    @Override
    public Optional<Patient> findById(UUID id) {
        return jpaRepo.findById(id);
    }

    @Override
    public List<Patient> findAll() {
        return jpaRepo.findAll();
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepo.existsByEmail(email);
    }

    @Override
    public boolean existsByEmailAndNotId(String email, UUID id) {
        return jpaRepo.existsByEmailAndIdNot(email, id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepo.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepo.deleteById(id);
    }
}
