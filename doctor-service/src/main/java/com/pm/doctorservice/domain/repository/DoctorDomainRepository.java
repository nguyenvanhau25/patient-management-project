package com.pm.doctorservice.domain.repository;

import com.pm.doctorservice.domain.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorDomainRepository {
    Doctor save(Doctor doctor);
    List<Doctor> findAll();
    Optional<Doctor> findById(UUID id);
    boolean existsById(UUID id);
    Page<Doctor> findDoctorsWithFilters(String specialization, Integer experienceMin, String location,
                                       LocalDate availability, Double minRating, Pageable pageable);
    List<Doctor> findTopRatedDoctors(int limit, String specialization);
}
