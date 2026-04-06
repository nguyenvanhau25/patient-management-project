package com.pm.doctorservice.interfaces.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import com.pm.doctorservice.application.dto.DoctorImageRequestDTO;
import com.pm.doctorservice.application.dto.DoctorRequestDTO;
import com.pm.doctorservice.application.dto.DoctorResponseDTO;
import com.pm.doctorservice.application.dto.DoctorScheduleRequestDTO;
import com.pm.doctorservice.application.dto.DoctorScheduleResponseDTO;
import com.pm.doctorservice.application.dto.DoctorDetails;
import com.pm.doctorservice.application.dto.TimeSlot;
import com.pm.doctorservice.application.dto.ReviewRequest;
import com.pm.doctorservice.application.dto.ReviewResponse;
import com.pm.doctorservice.application.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctors")
@Tag(name = "Bác sĩ", description = "Các endpoint quản lý bác sĩ")
@RequiredArgsConstructor
@Validated
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @Operation(summary = "Tạo bác sĩ", description = "Tạo một bản ghi bác sĩ mới")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDTO> addDoctor(@RequestBody @Valid DoctorRequestDTO doctorRequest) {
        return ResponseEntity.ok(doctorService.addDoctor(doctorRequest));
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách bác sĩ", description = "Lấy danh sách bác sĩ (có thể lọc theo chuyên khoa)")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy bác sĩ theo ID", description = "Lấy thông tin chi tiết của một bác sĩ theo ID")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable UUID id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật bác sĩ", description = "Cập nhật thông tin bác sĩ theo ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable UUID id, @RequestBody @Valid DoctorRequestDTO doctorRequest) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, doctorRequest));
    }

    @PatchMapping("/{id}/image")
    @Operation(summary = "Cập nhật ảnh bác sĩ", description = "Cập nhật URL ảnh đại diện cho bác sĩ")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<DoctorResponseDTO> updateDoctorImage(
            @PathVariable UUID id,
            @RequestBody @Valid DoctorImageRequestDTO imageRequestDTO) {
        return ResponseEntity.ok(doctorService.updateDoctorImage(id, imageRequestDTO.getProfileImageUrl()));
    }

    // Schedule Endpoints
    @PostMapping("/{id}/schedules")
    @Operation(summary = "Thêm lịch trình bác sĩ", description = "Tạo lịch trình làm việc cho bác sĩ")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorScheduleResponseDTO> addSchedule(@PathVariable UUID id, @RequestBody @Valid DoctorScheduleRequestDTO scheduleRequest) {
        return ResponseEntity.ok(doctorService.addSchedule(id, scheduleRequest));
    }

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm bác sĩ", description = "Tìm kiếm nâng cao với các bộ lọc")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<DoctorResponseDTO>> searchDoctors(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) Integer experienceMin,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) LocalDate availability,
            @RequestParam(required = false) Double minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("rating").descending());
        Page<DoctorResponseDTO> doctors = doctorService.searchDoctors(specialization, experienceMin,
                location, availability, minRating, pageable);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}/details")
    @Operation(summary = "Lấy chi tiết bác sĩ", description = "Lấy thông tin toàn diện của bác sĩ")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<DoctorDetails> getDoctorDetails(@PathVariable UUID id) {
        DoctorDetails details = doctorService.getDoctorDetails(id);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/{id}/availability")
    @Operation(summary = "Lấy thời gian trống của bác sĩ", description = "Lấy các khung giờ trống của bác sĩ")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<TimeSlot>> getDoctorAvailability(@PathVariable UUID id,
                                                               @RequestParam LocalDate date,
                                                               @RequestParam(defaultValue = "30") Integer durationMinutes) {
        List<TimeSlot> slots = doctorService.getDoctorAvailability(id, date, durationMinutes);
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/top-rated")
    @Operation(summary = "Lấy bác sĩ đánh giá cao nhất", description = "Lấy danh sách bác sĩ có đánh giá cao nhất")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<DoctorResponseDTO>> getTopRatedDoctors(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String specialization) {

        List<DoctorResponseDTO> topDoctors = doctorService.getTopRatedDoctors(limit, specialization);
        return ResponseEntity.ok(topDoctors);
    }

    // Review system endpoints
    @PostMapping("/{id}/reviews")
    @Operation(summary = "Gửi đánh giá", description = "Gửi đánh giá cho bác sĩ")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ReviewResponse> submitReview(@PathVariable UUID id,
                                                      @RequestBody @Valid ReviewRequest request,
                                                      Authentication auth) {
        String patientId = auth.getName();
        ReviewResponse response = doctorService.submitReview(id, patientId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/reviews")
    @Operation(summary = "Lấy đánh giá bác sĩ", description = "Lấy danh sách đánh giá của một bác sĩ")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<ReviewResponse>> getDoctorReviews(@PathVariable UUID id,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        List<ReviewResponse> reviews = doctorService.getDoctorReviews(id, page, size);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/reviews/{reviewId}/response")
    @Operation(summary = "Phản hồi đánh giá", description = "Bác sĩ hoặc Admin phản hồi lại đánh giá")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReviewResponse> respondToReview(@PathVariable UUID reviewId,
                                                         @RequestBody @NotBlank(message = "Phản hồi không được để trống") String doctorResponse) {
        ReviewResponse response = doctorService.respondToReview(reviewId, doctorResponse);
        return ResponseEntity.ok(response);
    }
}
