package com.pm.doctorservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDetails {
    private UUID id;
    private String fullName;
    private String specialization;
    private String bio;
    private String qualifications;
    private Integer experienceYears;
    private String location;
    private Double consultationFee;
    private Double rating;
    private Integer totalReviews;
    private String profileImageUrl;
    private LocalDate nextAvailableSlot;
    private Integer responseTimeHours; // Average response time
    private Boolean available;
}
