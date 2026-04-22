package com.pm.clinicalservice.application.service;

import com.pm.clinicalservice.application.dto.MedicalRecordRequestDTO;
import com.pm.clinicalservice.application.dto.MedicalRecordResponseDTO;
import com.pm.clinicalservice.application.mapper.MedicalRecordMapper;
import com.pm.clinicalservice.domain.model.MedicalRecord;
import com.pm.clinicalservice.domain.repository.MedicalRecordRepository;
import com.pm.clinicalservice.interfaces.client.DoctorClient;
import com.pm.clinicalservice.interfaces.client.PatientClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientClient patientClient;
    private final DoctorClient doctorClient;

    public MedicalRecordResponseDTO create(MedicalRecordRequestDTO request) {
        validateDependencies(request.getPatientId(), request.getDoctorId());
        MedicalRecord medicalRecord = MedicalRecordMapper.toModel(request);
        return MedicalRecordMapper.toDTO(medicalRecordRepository.save(medicalRecord));
    }

    public MedicalRecordResponseDTO getById(UUID id) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical record not found"));
        return MedicalRecordMapper.toDTO(record);
    }

    public List<MedicalRecordResponseDTO> getAll() {
        return medicalRecordRepository.findAll().stream()
                .map(MedicalRecordMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<MedicalRecordResponseDTO> getByPatientId(UUID patientId) {
        return medicalRecordRepository.findByPatientId(patientId).stream()
                .map(MedicalRecordMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MedicalRecordResponseDTO update(UUID id, MedicalRecordRequestDTO update) {
        MedicalRecord existing = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical record not found"));
        if (update.getPatientId() != null && !patientClient.checkPatientExists(update.getPatientId())) {
            throw new RuntimeException("Không tìm thấy bệnh nhân: " + update.getPatientId());
        }
        if (update.getDoctorId() != null && !doctorClient.checkDoctorExists(update.getDoctorId())) {
            throw new RuntimeException("Không tìm thấy bác sĩ: " + update.getDoctorId());
        }
        if (update.getDiagnosis() != null) existing.setDiagnosis(update.getDiagnosis());
        if (update.getSymptoms() != null) existing.setSymptoms(update.getSymptoms());
        if (update.getNotes() != null) existing.setNotes(update.getNotes());
        if (update.getVisitDate() != null) existing.setVisitDate(update.getVisitDate());
        return MedicalRecordMapper.toDTO(medicalRecordRepository.save(existing));
    }

    public boolean existsById(UUID id) {
        return medicalRecordRepository.existsById(id);
    }

    private void validateDependencies(UUID patientId, UUID doctorId) {
        if (patientId == null || !patientClient.checkPatientExists(patientId)) {
            throw new RuntimeException("Không tìm thấy bệnh nhân: " + patientId);
        }
        if (doctorId == null || !doctorClient.checkDoctorExists(doctorId)) {
            throw new RuntimeException("Không tìm thấy bác sĩ: " + doctorId);
        }
    }
}
