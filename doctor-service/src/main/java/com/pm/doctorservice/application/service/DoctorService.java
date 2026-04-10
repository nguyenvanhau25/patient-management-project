package com.pm.doctorservice.application.service;

import com.pm.doctorservice.application.dto.DoctorRequestDTO;
import com.pm.doctorservice.application.dto.DoctorResponseDTO;
import com.pm.doctorservice.application.dto.DoctorScheduleRequestDTO;
import com.pm.doctorservice.application.dto.DoctorScheduleResponseDTO;
import com.pm.doctorservice.application.mapper.DoctorMapper;
import com.pm.doctorservice.application.mapper.DoctorScheduleMapper;
import com.pm.doctorservice.application.dto.*;
import com.pm.doctorservice.domain.model.*;
import com.pm.doctorservice.domain.repository.DoctorDomainRepository;
import com.pm.doctorservice.domain.repository.DoctorReviewRepository;
import com.pm.doctorservice.domain.repository.DoctorScheduleDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorDomainRepository doctorRepository;
    private final DoctorScheduleDomainRepository doctorScheduleRepository;
    private final DoctorReviewRepository doctorReviewRepository;
    private final FileStorageService fileStorageService;

    public void uploadImage(UUID id, org.springframework.web.multipart.MultipartFile file) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));
        String fileName = fileStorageService.storeFile(file);
        // Map to a URL that can be served (e.g., /api/doctors/uploads/filename)
        String fileUrl = "/api/doctors/uploads/" + fileName;
        doctor.setProfileImageUrl(fileUrl);
        doctorRepository.save(doctor);
    }

    public DoctorResponseDTO addDoctor(DoctorRequestDTO request) {
        Doctor doctor = DoctorMapper.toModel(request);
        return DoctorMapper.toDTO(doctorRepository.save(doctor));
    }

    public List<DoctorResponseDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(DoctorMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DoctorResponseDTO getDoctorById(UUID id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));
        return DoctorMapper.toDTO(doctor);
    }

    public DoctorResponseDTO updateDoctor(UUID id, DoctorRequestDTO request) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));
        if (request.getName() != null) doctor.setName(request.getName());
        if (request.getEmail() != null) doctor.setEmail(request.getEmail());
        if (request.getSpecialization() != null) doctor.setSpecialization(request.getSpecialization());
        if (request.getPhoneNumber() != null) doctor.setPhoneNumber(request.getPhoneNumber());
        if (request.getExperienceYears() != null) doctor.setExperienceYears(request.getExperienceYears());
        if (request.getProfileImageUrl() != null && !request.getProfileImageUrl().isBlank()) {
            doctor.setProfileImageUrl(request.getProfileImageUrl());
        }
        return DoctorMapper.toDTO(doctorRepository.save(doctor));
    }

    // Schedule Management
    public DoctorScheduleResponseDTO addSchedule(UUID doctorId, DoctorScheduleRequestDTO request) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));
        DoctorSchedule schedule = DoctorScheduleMapper.toModel(request);
        schedule.setDoctor(doctor);
        schedule.setIsAvailable(true);
        return DoctorScheduleMapper.toDTO(doctorScheduleRepository.save(schedule));
    }

    public List<DoctorScheduleResponseDTO> getDoctorSchedules(UUID doctorId) {
        return doctorScheduleRepository.findByDoctorId(doctorId).stream()
                .map(DoctorScheduleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<DoctorScheduleResponseDTO> getDoctorSchedulesByDate(UUID doctorId, LocalDate date) {
        return doctorScheduleRepository.findByDoctorIdAndWorkDate(doctorId, date).stream()
                .map(DoctorScheduleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean existsById(UUID doctorId) {
        return doctorRepository.existsById(doctorId);
    }

    public DoctorResponseDTO updateDoctorImage(UUID id, String profileImageUrl) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));
        doctor.setProfileImageUrl(profileImageUrl);
        return DoctorMapper.toDTO(doctorRepository.save(doctor));
    }

    // Enhanced methods for better user experience
    public Page<DoctorResponseDTO> searchDoctors(String specialization, Integer experienceMin, String location,
                                                LocalDate availability, Double minRating, Pageable pageable) {
        Page<Doctor> doctors = doctorRepository.findDoctorsWithFilters(
                specialization, experienceMin, location, availability, minRating, pageable);
        return doctors.map(DoctorMapper::toDTO);
    }

    public DoctorDetails getDoctorDetails(UUID doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        LocalDate nextAvailableSlot = findNextAvailableSlot(doctor);
        Integer responseTimeHours = calculateAverageResponseTime(doctor);

        return DoctorDetails.builder()
                .id(doctor.getId())
                .fullName(doctor.getFullName())
                .specialization(doctor.getSpecialization())
                .bio(doctor.getBio())
                .qualifications(doctor.getQualifications())
                .experienceYears(doctor.getExperienceYears())
                .location(doctor.getLocation())
                .consultationFee(doctor.getConsultationFee())
                .rating(doctor.getRating())
                .totalReviews(doctor.getTotalReviews())
                .profileImageUrl(doctor.getProfileImageUrl())
                .nextAvailableSlot(nextAvailableSlot)
                .responseTimeHours(responseTimeHours)
                .available(doctor.getAvailable())
                .build();
    }

    public List<TimeSlot> getDoctorAvailability(UUID doctorId, LocalDate date, Integer durationMinutes) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        int slotDuration = (durationMinutes == null) ? 30 : durationMinutes; // default 30 minutes

        return doctor.getSchedules().stream()
                .filter(schedule -> schedule.getWorkDate().equals(date) && schedule.getIsAvailable())
                .flatMap(schedule -> generateTimeSlots(schedule, slotDuration).stream())
                .collect(Collectors.toList());
    }

    public List<DoctorResponseDTO> getTopRatedDoctors(int limit, String specialization) {
        List<Doctor> doctors = doctorRepository.findTopRatedDoctors(limit, specialization);
        return doctors.stream().map(DoctorMapper::toDTO).toList();
    }

    // Review system methods
    public ReviewResponse submitReview(UUID doctorId, String patientId, ReviewRequest request) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        // Validate rating
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("Đánh giá phải nằm trong khoảng từ 1 đến 5");
        }

        DoctorReview review = DoctorReview.builder()
                .doctor(doctor)
                .patientId(patientId)
                .rating(request.getRating())
                .title(request.getTitle())
                .comment(request.getComment())
                .appointmentId(request.getAppointmentId())
                .tags(request.getTags())
                .wouldRecommend(request.getWouldRecommend())
                .build();

        review = doctorReviewRepository.save(review);

        // Update doctor's rating
        doctor.updateRating(request.getRating());
        doctorRepository.save(doctor);

        return ReviewResponse.builder()
                .id(review.getId())
                .patientId(review.getPatientId())
                .rating(review.getRating())
                .title(review.getTitle())
                .comment(review.getComment())
                .tags(review.getTags())
                .wouldRecommend(review.getWouldRecommend())
                .helpfulCount(review.getHelpfulCount())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public List<ReviewResponse> getDoctorReviews(UUID doctorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<DoctorReview> reviews = doctorReviewRepository.findByDoctorIdOrderByCreatedAtDesc(doctorId, pageable);

        return reviews.stream()
                .map(review -> ReviewResponse.builder()
                        .id(review.getId())
                        .patientId(review.getPatientId())
                        .rating(review.getRating())
                        .title(review.getTitle())
                        .comment(review.getComment())
                        .tags(review.getTags())
                        .wouldRecommend(review.getWouldRecommend())
                        .helpfulCount(review.getHelpfulCount())
                        .createdAt(review.getCreatedAt())
                        .doctorResponse(review.getDoctorResponse())
                        .doctorResponseAt(review.getDoctorResponseAt())
                        .build())
                .toList();
    }

    public ReviewResponse respondToReview(UUID reviewId, String doctorResponse) {
        DoctorReview review = doctorReviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá"));

        review.setDoctorResponse(doctorResponse);
        review.setDoctorResponseAt(LocalDateTime.now());
        review = doctorReviewRepository.save(review);

        return ReviewResponse.builder()
                .id(review.getId())
                .patientId(review.getPatientId())
                .rating(review.getRating())
                .title(review.getTitle())
                .comment(review.getComment())
                .tags(review.getTags())
                .wouldRecommend(review.getWouldRecommend())
                .helpfulCount(review.getHelpfulCount())
                .createdAt(review.getCreatedAt())
                .doctorResponse(review.getDoctorResponse())
                .doctorResponseAt(review.getDoctorResponseAt())
                .build();
    }

    // Helper methods
    private LocalDate findNextAvailableSlot(Doctor doctor) {
        return doctor.getSchedules().stream()
                .filter(DoctorSchedule::getIsAvailable)
                .filter(schedule -> schedule.getWorkDate().isAfter(LocalDate.now()) ||
                                   (schedule.getWorkDate().equals(LocalDate.now()) &&
                                    schedule.getStartTime().isAfter(LocalTime.now())))
                .map(DoctorSchedule::getWorkDate)
                .min(LocalDate::compareTo)
                .orElse(null);
    }

    private Integer calculateAverageResponseTime(Doctor doctor) {
        // Mock calculation - in real implementation, this would be based on actual response times
        return doctor.getTotalReviews() > 0 ? 2 : 4; // hours
    }

    private List<TimeSlot> generateTimeSlots(DoctorSchedule schedule, int durationMinutes) {
        List<TimeSlot> slots = new ArrayList<>();
        LocalTime current = schedule.getStartTime();

        while (!current.plusMinutes(durationMinutes).isAfter(schedule.getEndTime())) {
            LocalTime endTime = current.plusMinutes(durationMinutes);
            slots.add(TimeSlot.builder()
                    .startTime(current)
                    .endTime(endTime)
                    .available(true)
                    .durationMinutes(durationMinutes)
                    .build());
            current = endTime;
        }

        return slots;
    }
}
