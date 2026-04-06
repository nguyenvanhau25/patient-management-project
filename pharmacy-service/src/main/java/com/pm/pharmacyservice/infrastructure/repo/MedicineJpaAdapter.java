package com.pm.pharmacyservice.infrastructure.repo;

import com.pm.pharmacyservice.domain.model.Medicine;
import com.pm.pharmacyservice.domain.repository.MedicineRepository;
import com.pm.pharmacyservice.infrastructure.repo.jpa.MedicineJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MedicineJpaAdapter implements MedicineRepository {
    private final MedicineJpaRepository jpaRepository;

    @Override
    public Medicine save(Medicine medicine) {
        return jpaRepository.save(medicine);
    }

    @Override
    public List<Medicine> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Medicine> findById(UUID id) {
        return jpaRepository.findById(id);
    }
}
