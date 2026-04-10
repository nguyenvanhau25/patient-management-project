package com.pm.pharmacyservice.application.service;

import com.pm.pharmacyservice.application.dto.MedicineRequestDTO;
import com.pm.pharmacyservice.application.dto.MedicineResponseDTO;
import com.pm.pharmacyservice.application.dto.PrescriptionRequestDTO;
import com.pm.pharmacyservice.application.dto.PrescriptionResponseDTO;
import com.pm.pharmacyservice.application.mapper.PharmacyMapper;
import com.pm.pharmacyservice.domain.model.Medicine;
import com.pm.pharmacyservice.domain.model.Prescription;
import com.pm.pharmacyservice.domain.repository.MedicineRepository;
import com.pm.pharmacyservice.domain.repository.PrescriptionRepository;
import com.pm.pharmacyservice.interfaces.client.ClinicalClient;
import com.pm.pharmacyservice.interfaces.client.DoctorClient;
import com.pm.pharmacyservice.interfaces.client.PatientClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PharmacyService {
    private final MedicineRepository medicineRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final DoctorClient doctorClient;
    private final PatientClient patientClient;
    private final ClinicalClient clinicalClient;
    private final FileStorageService fileStorageService;

    /**
     * Tải lên và cập nhật ảnh cho thuốc từ tệp vật lý.
     */
    public void uploadMedicineImage(UUID medicineId, org.springframework.web.multipart.MultipartFile file) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc với ID: " + medicineId));
        String fileName = fileStorageService.storeFile(file);
        // Ánh xạ đến URL có thể phục vụ
        String fileUrl = "/api/pharmacy/uploads/" + fileName;
        medicine.setImageUrl(fileUrl);
        medicineRepository.save(medicine);
    }

    public List<MedicineResponseDTO> listMedicines() {
        return medicineRepository.findAll().stream()
                .map(PharmacyMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MedicineResponseDTO addMedicine(MedicineRequestDTO request) {
        Medicine medicine = PharmacyMapper.toModel(request);
        return PharmacyMapper.toDTO(medicineRepository.save(medicine));
    }

    public PrescriptionResponseDTO prescribe(PrescriptionRequestDTO request) {
        if (request.getDoctorId() == null || !doctorClient.checkDoctorExists(request.getDoctorId())) {
            throw new RuntimeException("Doctor not found: " + request.getDoctorId());
        }
        if (request.getPatientId() == null || !patientClient.checkPatientExists(request.getPatientId())) {
            throw new RuntimeException("Patient not found: " + request.getPatientId());
        }
        if (request.getMedicalRecordId() != null
                && !clinicalClient.checkMedicalRecordExists(request.getMedicalRecordId())) {
            throw new RuntimeException("Medical record not found: " + request.getMedicalRecordId());
        }
        Prescription prescription = PharmacyMapper.toModel(request);
        return PharmacyMapper.toDTO(prescriptionRepository.save(prescription));
    }

    public List<PrescriptionResponseDTO> getPrescriptionsByPatient(UUID patientId) {
        return prescriptionRepository.findByPatientId(patientId).stream()
                .map(PharmacyMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PrescriptionResponseDTO dispense(UUID prescriptionId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        if (prescription.getItems() != null) {
            prescription.getItems().forEach(item -> {
                Medicine medicine = medicineRepository.findById(item.getMedicineId())
                        .orElseThrow(() -> new RuntimeException("Medicine not found: " + item.getMedicineId()));
                if (medicine.getQuantity() < item.getQuantity()) {
                    throw new RuntimeException("Not enough stock for medicine " + medicine.getName());
                }
                medicine.setQuantity(medicine.getQuantity() - item.getQuantity());
                medicineRepository.save(medicine);
            });
        }

        return PharmacyMapper.toDTO(prescription);
    }
}
