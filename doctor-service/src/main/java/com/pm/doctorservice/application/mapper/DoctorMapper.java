package com.pm.doctorservice.application.mapper;

import com.pm.doctorservice.application.dto.DoctorRequestDTO;
import com.pm.doctorservice.application.dto.DoctorResponseDTO;
import com.pm.doctorservice.domain.model.Doctor;

public class DoctorMapper {
    public static DoctorResponseDTO toDTO(Doctor doctor) {
        if (doctor == null) return null;
        DoctorResponseDTO dto = new DoctorResponseDTO();
        dto.setId(doctor.getId() != null ? doctor.getId().toString() : null);
        dto.setName(doctor.getName());
        dto.setEmail(doctor.getEmail());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setPhoneNumber(doctor.getPhoneNumber());
        dto.setExperienceYears(doctor.getExperienceYears());
        dto.setProfileImageUrl(doctor.getProfileImageUrl());
        return dto;
    }

    public static Doctor toModel(DoctorRequestDTO dto) {
        if (dto == null) return null;
        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setEmail(dto.getEmail());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setPhoneNumber(dto.getPhoneNumber());
        doctor.setExperienceYears(dto.getExperienceYears());
        doctor.setProfileImageUrl(dto.getProfileImageUrl());
        return doctor;
    }
}
