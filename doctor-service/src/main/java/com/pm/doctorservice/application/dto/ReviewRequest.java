package com.pm.doctorservice.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    @NotNull(message = "Đánh giá là bắt buộc")
    @Min(value = 1, message = "Đánh giá phải từ 1")
    @Max(value = 5, message = "Đánh giá không được vượt quá 5")
    private Integer rating;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 100, message = "Tiêu đề không được vượt quá 100 ký tự")
    private String title;

    @NotBlank(message = "Nhận xét không được để trống")
    @Size(max = 1000, message = "Nhận xét không được vượt quá 1000 ký tự")
    private String comment;

    @NotBlank(message = "ID lịch hẹn không được để trống")
    private String appointmentId;

    private List<String> tags;
    private Boolean wouldRecommend;
}
