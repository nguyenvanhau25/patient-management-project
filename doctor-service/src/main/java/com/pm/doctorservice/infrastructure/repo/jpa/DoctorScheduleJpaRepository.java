package com.pm.doctorservice.infrastructure.repo.jpa;

import com.pm.doctorservice.domain.model.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DoctorScheduleJpaRepository extends JpaRepository<DoctorSchedule, UUID> {
    List<DoctorSchedule> findByDoctorIdAndWorkDate(UUID doctorId, LocalDate workDate);
    List<DoctorSchedule> findByDoctorId(UUID doctorId);
}
