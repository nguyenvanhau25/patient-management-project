package com.pm.doctorservice.infrastructure.repo;

import com.pm.doctorservice.domain.model.Doctor;
import com.pm.doctorservice.domain.repository.DoctorDomainRepository;
import com.pm.doctorservice.infrastructure.repo.jpa.DoctorJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DoctorJpaAdapter implements DoctorDomainRepository {
    private final DoctorJpaRepository jpaRepository;

    @Override
    public Doctor save(Doctor doctor) {
        return jpaRepository.save(doctor);
    }

    @Override
    public List<Doctor> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Doctor> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Page<Doctor> findDoctorsWithFilters(String specialization, Integer experienceMin, String location,
                                              LocalDate availability, Double minRating, Pageable pageable) {
        // Implement complex filtering here, for now return paged results
        return jpaRepository.findAll(pageable);
    }

    @Override
    public List<Doctor> findTopRatedDoctors(int limit, String specialization) {
        // Implement top rated logic here
        return jpaRepository.findAll().stream()
                .filter(d -> specialization == null || d.getSpecialization().equalsIgnoreCase(specialization))
                .sorted((d1, d2) -> d2.getRating().compareTo(d1.getRating()))
                .limit(limit)
                .toList();
    }
}
