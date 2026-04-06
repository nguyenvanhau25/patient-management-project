package com.pm.doctorservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private UUID id;
    private String patientId;
    private Integer rating;
    private String title;
    private String comment;
    private List<String> tags;
    private Boolean wouldRecommend;
    private Integer helpfulCount;
    private LocalDateTime createdAt;
    private String doctorResponse;
    private LocalDateTime doctorResponseAt;
}
