package com.pm.clinicalservice.application.mapper;

import com.pm.clinicalservice.application.dto.MedicalRecordRequestDTO;
import com.pm.clinicalservice.application.dto.MedicalRecordResponseDTO;
import com.pm.clinicalservice.domain.model.MedicalRecord;

public class MedicalRecordMapper {
    public static MedicalRecordResponseDTO toDTO(MedicalRecord record) {
        if (record == null) return null;
        MedicalRecordResponseDTO dto = new MedicalRecordResponseDTO();
        dto.setId(record.getId() != null ? record.getId().toString() : null);
        dto.setPatientId(record.getPatientId() != null ? record.getPatientId().toString() : null);
        dto.setDoctorId(record.getDoctorId() != null ? record.getDoctorId().toString() : null);
        dto.setVisitDate(record.getVisitDate());
        dto.setDiagnosis(record.getDiagnosis());
        dto.setSymptoms(record.getSymptoms());
        dto.setNotes(record.getNotes());
        dto.setCreatedAt(record.getCreatedAt());
        dto.setUpdatedAt(record.getUpdatedAt());
        return dto;
    }

    public static MedicalRecord toModel(MedicalRecordRequestDTO dto) {
        if (dto == null) return null;
        MedicalRecord record = new MedicalRecord();
        record.setPatientId(dto.getPatientId());
        record.setDoctorId(dto.getDoctorId());
        record.setVisitDate(dto.getVisitDate());
        record.setDiagnosis(dto.getDiagnosis());
        record.setSymptoms(dto.getSymptoms());
        record.setNotes(dto.getNotes());
        return record;
    }
}
