package com.pm.doctorservice.domain.repository;

import com.pm.doctorservice.domain.model.DoctorReview;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorReviewRepository extends JpaRepository<DoctorReview, UUID> {

    List<DoctorReview> findByDoctorIdOrderByCreatedAtDesc(UUID doctorId, Pageable pageable);

    List<DoctorReview> findByDoctorId(UUID doctorId);

    @Query("SELECT AVG(r.rating) FROM DoctorReview r WHERE r.doctor.id = :doctorId")
    Double findAverageRatingByDoctorId(@Param("doctorId") UUID doctorId);

    @Query("SELECT COUNT(r) FROM DoctorReview r WHERE r.doctor.id = :doctorId")
    Long countReviewsByDoctorId(@Param("doctorId") UUID doctorId);
}