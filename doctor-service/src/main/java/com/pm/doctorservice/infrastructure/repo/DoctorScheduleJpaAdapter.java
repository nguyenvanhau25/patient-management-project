package com.pm.doctorservice.infrastructure.repo;

import com.pm.doctorservice.domain.model.DoctorSchedule;
import com.pm.doctorservice.domain.repository.DoctorScheduleDomainRepository;
import com.pm.doctorservice.infrastructure.repo.jpa.DoctorScheduleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DoctorScheduleJpaAdapter implements DoctorScheduleDomainRepository {
    private final DoctorScheduleJpaRepository jpaRepository;

    @Override
    public DoctorSchedule save(DoctorSchedule doctorSchedule) {
        return jpaRepository.save(doctorSchedule);
    }

    @Override
    public List<DoctorSchedule> findByDoctorId(UUID doctorId) {
        return jpaRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<DoctorSchedule> findByDoctorIdAndWorkDate(UUID doctorId, LocalDate workDate) {
        return jpaRepository.findByDoctorIdAndWorkDate(doctorId, workDate);
    }
}
