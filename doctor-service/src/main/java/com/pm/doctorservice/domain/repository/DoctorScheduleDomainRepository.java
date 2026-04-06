package com.pm.doctorservice.domain.repository;

import com.pm.doctorservice.domain.model.DoctorSchedule;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DoctorScheduleDomainRepository {
    DoctorSchedule save(DoctorSchedule doctorSchedule);
    List<DoctorSchedule> findByDoctorId(UUID doctorId);
    List<DoctorSchedule> findByDoctorIdAndWorkDate(UUID doctorId, LocalDate workDate);
}
