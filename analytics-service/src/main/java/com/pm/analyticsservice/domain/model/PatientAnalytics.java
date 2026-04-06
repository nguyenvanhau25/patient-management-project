package com.pm.analyticsservice.domain.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
public class PatientAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String patientId;
    private String name;
    private String email;

    private LocalDate createdDate;
    // hành vi nghiệp vụ, thuần nghiệp vụ
    public static PatientAnalytics fromEvent(
            String patientId,
            String name,
            String email
    ) {
        PatientAnalytics pa = new PatientAnalytics();
        pa.patientId = patientId;
        pa.name = name;
        pa.email = email;
        pa.createdDate = LocalDate.now();
        return pa;
    }

    public boolean isCreatedOn(LocalDate date) {
        return createdDate.equals(date);
    }
}
