package com.pm.doctorservice.interfaces.rest;

import com.pm.doctorservice.application.dto.DoctorDetails;
import com.pm.doctorservice.application.dto.DoctorImageRequestDTO;
import com.pm.doctorservice.application.dto.DoctorRequestDTO;
import com.pm.doctorservice.application.dto.DoctorResponseDTO;
import com.pm.doctorservice.application.dto.DoctorScheduleRequestDTO;
import com.pm.doctorservice.application.dto.DoctorScheduleResponseDTO;
import com.pm.doctorservice.application.dto.ReviewRequest;
import com.pm.doctorservice.application.dto.ReviewResponse;
import com.pm.doctorservice.application.dto.TimeSlot;
import com.pm.doctorservice.application.service.DoctorService;
import com.pm.doctorservice.infrastructure.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/doctors")
@Tag(name = "Bac si", description = "Cac endpoint quan ly bac si")
@RequiredArgsConstructor
@Validated
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @Operation(summary = "Tao bac si", description = "Tao mot ban ghi bac si moi")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DoctorResponseDTO>> addDoctor(@RequestBody @Valid DoctorRequestDTO doctorRequest) {
        return ResponseEntity.ok(ApiResponse.success(doctorService.addDoctor(doctorRequest)));
    }

    @GetMapping
    @Operation(summary = "Lay danh sach bac si", description = "Lay danh sach bac si")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<DoctorResponseDTO>>> getAllDoctors() {
        return ResponseEntity.ok(ApiResponse.success(doctorService.getAllDoctors()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lay bac si theo ID", description = "Lay thong tin chi tiet cua mot bac si theo ID")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<DoctorResponseDTO>> getDoctorById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(doctorService.getDoctorById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cap nhat bac si", description = "Cap nhat thong tin bac si theo ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable UUID id, @RequestBody @Valid DoctorRequestDTO doctorRequest) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, doctorRequest));
    }

    @PatchMapping("/{id}/image")
    @Operation(summary = "Cap nhat anh bac si", description = "Cap nhat URL anh dai dien cho bac si")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<DoctorResponseDTO> updateDoctorImage(
            @PathVariable UUID id,
            @RequestBody @Valid DoctorImageRequestDTO imageRequestDTO) {
        return ResponseEntity.ok(doctorService.updateDoctorImage(id, imageRequestDTO.getProfileImageUrl()));
    }

    @PostMapping("/{id}/upload-image")
    @Operation(summary = "Tai len anh bac si", description = "Tai len tep anh vat ly cho bac si")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<String> uploadDoctorImage(
            @PathVariable UUID id,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        doctorService.uploadImage(id, file);
        return ResponseEntity.ok("File uploaded successfully");
    }

    @PostMapping("/{id}/schedules")
    @Operation(summary = "Them lich trinh bac si", description = "Tao lich trinh lam viec cho bac si")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorScheduleResponseDTO> addSchedule(@PathVariable UUID id, @RequestBody @Valid DoctorScheduleRequestDTO scheduleRequest) {
        return ResponseEntity.ok(doctorService.addSchedule(id, scheduleRequest));
    }

    @GetMapping("/{id}/schedules")
    @Operation(summary = "Lay lich trinh bac si", description = "Lay lich lam viec cua bac si, co the loc theo ngay")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<DoctorScheduleResponseDTO>>> getDoctorSchedules(
            @PathVariable UUID id,
            @RequestParam(required = false) LocalDate date) {
        List<DoctorScheduleResponseDTO> schedules = date == null
                ? doctorService.getDoctorSchedules(id)
                : doctorService.getDoctorSchedulesByDate(id, date);
        return ResponseEntity.ok(ApiResponse.success(schedules));
    }

    @GetMapping("/search")
    @Operation(summary = "Tim kiem bac si", description = "Tim kiem nang cao voi cac bo loc")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<DoctorResponseDTO>>> searchDoctors(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false, name = "experienceMin") Integer experienceMin,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) LocalDate availability,
            @RequestParam(required = false, name = "minRating") Double minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("rating").descending());
        Page<DoctorResponseDTO> doctorPage = doctorService.searchDoctors(
                specialization, experienceMin, location, availability, minRating, pageable);

        Map<String, Object> meta = new HashMap<>();
        meta.put("totalElements", doctorPage.getTotalElements());
        meta.put("totalPages", doctorPage.getTotalPages());
        meta.put("pageNumber", doctorPage.getNumber());
        meta.put("pageSize", doctorPage.getSize());

        return ResponseEntity.ok(ApiResponse.<List<DoctorResponseDTO>>builder()
                .code("SUCCESS")
                .message("Thanh cong")
                .data(doctorPage.getContent())
                .meta(meta)
                .build());
    }

    @GetMapping("/{id}/details")
    @Operation(summary = "Lay chi tiet bac si", description = "Lay thong tin toan dien cua bac si")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<DoctorDetails> getDoctorDetails(@PathVariable UUID id) {
        DoctorDetails details = doctorService.getDoctorDetails(id);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/{id}/availability")
    @Operation(summary = "Lay thoi gian trong cua bac si", description = "Lay cac khung gio trong cua bac si")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<TimeSlot>>> getDoctorAvailability(
            @PathVariable UUID id,
            @RequestParam LocalDate date,
            @RequestParam(defaultValue = "30") Integer durationMinutes) {
        List<TimeSlot> slots = doctorService.getDoctorAvailability(id, date, durationMinutes);
        return ResponseEntity.ok(ApiResponse.success(slots));
    }

    @GetMapping("/top-rated")
    @Operation(summary = "Lay bac si danh gia cao nhat", description = "Lay danh sach bac si co danh gia cao nhat")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<DoctorResponseDTO>> getTopRatedDoctors(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String specialization) {
        List<DoctorResponseDTO> topDoctors = doctorService.getTopRatedDoctors(limit, specialization);
        return ResponseEntity.ok(topDoctors);
    }

    @PostMapping("/{id}/reviews")
    @Operation(summary = "Gui danh gia", description = "Gui danh gia cho bac si")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ReviewResponse> submitReview(
            @PathVariable UUID id,
            @RequestBody @Valid ReviewRequest request,
            Authentication auth) {
        String patientId = auth.getName();
        ReviewResponse response = doctorService.submitReview(id, patientId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/reviews")
    @Operation(summary = "Lay danh gia bac si", description = "Lay danh sach danh gia cua mot bac si")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<ReviewResponse>> getDoctorReviews(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<ReviewResponse> reviews = doctorService.getDoctorReviews(id, page, size);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/reviews/{reviewId}/response")
    @Operation(summary = "Phan hoi danh gia", description = "Bac si hoac admin phan hoi lai danh gia")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReviewResponse> respondToReview(
            @PathVariable UUID reviewId,
            @RequestBody @NotBlank(message = "Phan hoi khong duoc de trong") String doctorResponse) {
        ReviewResponse response = doctorService.respondToReview(reviewId, doctorResponse);
        return ResponseEntity.ok(response);
    }
}
