package com.pm.pharmacyservice.domain.repository;

import com.pm.pharmacyservice.domain.model.Medicine;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MedicineRepository {
    Medicine save(Medicine medicine);
    List<Medicine> findAll();
    Optional<Medicine> findById(UUID id);
}
