package com.pm.pharmacyservice.infrastructure.repo.jpa;

import com.pm.pharmacyservice.domain.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MedicineJpaRepository extends JpaRepository<Medicine, UUID> {
}
