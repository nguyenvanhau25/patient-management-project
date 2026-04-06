package com.pm.doctorservice.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String specialization;

    private String phoneNumber;

    private Integer experienceYears;

    private String profileImageUrl;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(length = 1000)
    private String bio;

    @Column(length = 500)
    private String qualifications;

    @Column
    private String licenseNumber;

    @Column
    private String location;

    @Column
    private Double consultationFee;

    @Column
    private Double rating;

    @Column
    private Integer totalReviews;

    @Column
    private Boolean available;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoctorSchedule> schedules;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DoctorReview> reviews;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (rating == null) rating = 0.0;
        if (totalReviews == null) totalReviews = 0;
        if (available == null) available = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return name;
    }

    public void updateRating(double newRating) {
        if (totalReviews == 0) {
            this.rating = newRating;
        } else {
            this.rating = (this.rating * totalReviews + newRating) / (totalReviews + 1);
        }
        this.totalReviews++;
    }
}
