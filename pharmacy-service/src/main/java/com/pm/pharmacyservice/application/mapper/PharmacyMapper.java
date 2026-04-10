package com.pm.pharmacyservice.application.mapper;

import com.pm.pharmacyservice.application.dto.MedicineRequestDTO;
import com.pm.pharmacyservice.application.dto.MedicineResponseDTO;
import com.pm.pharmacyservice.application.dto.PrescriptionRequestDTO;
import com.pm.pharmacyservice.application.dto.PrescriptionResponseDTO;
import com.pm.pharmacyservice.domain.model.Medicine;
import com.pm.pharmacyservice.domain.model.Prescription;

import java.util.stream.Collectors;

public class PharmacyMapper {

    public static MedicineResponseDTO toDTO(Medicine medicine) {
        if (medicine == null) return null;
        MedicineResponseDTO dto = new MedicineResponseDTO();
        dto.setId(medicine.getId() != null ? medicine.getId().toString() : null);
        dto.setName(medicine.getName());
        dto.setManufacturer(medicine.getManufacturer());
        dto.setPrice(medicine.getPrice());
        dto.setQuantity(medicine.getQuantity());
        dto.setImageUrl(medicine.getImageUrl());
        return dto;
    }

    public static Medicine toModel(MedicineRequestDTO dto) {
        if (dto == null) return null;
        Medicine medicine = new Medicine();
        medicine.setName(dto.getName());
        medicine.setManufacturer(dto.getManufacturer());
        medicine.setPrice(dto.getPrice());
        medicine.setQuantity(dto.getQuantity());
        medicine.setImageUrl(dto.getImageUrl());
        return medicine;
    }

    public static PrescriptionResponseDTO toDTO(Prescription prescription) {
        if (prescription == null) return null;
        PrescriptionResponseDTO dto = new PrescriptionResponseDTO();
        dto.setId(prescription.getId() != null ? prescription.getId().toString() : null);
        dto.setPatientId(prescription.getPatientId() != null ? prescription.getPatientId().toString() : null);
        dto.setDoctorId(prescription.getDoctorId() != null ? prescription.getDoctorId().toString() : null);
        dto.setMedicalRecordId(prescription.getMedicalRecordId() != null ? prescription.getMedicalRecordId().toString() : null);
        
        if (prescription.getItems() != null) {
            dto.setItems(prescription.getItems().stream().map(item -> {
                PrescriptionResponseDTO.PrescriptionItemDTO itemDTO = new PrescriptionResponseDTO.PrescriptionItemDTO();
                itemDTO.setMedicineId(item.getMedicineId() != null ? item.getMedicineId().toString() : null);
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setDosage(item.getDosage());
                itemDTO.setInstructions(item.getInstructions());
                return itemDTO;
            }).collect(Collectors.toList()));
        }
        
        dto.setCreatedAt(prescription.getCreatedAt());
        return dto;
    }

    public static Prescription toModel(PrescriptionRequestDTO dto) {
        if (dto == null) return null;
        Prescription prescription = new Prescription();
        prescription.setPatientId(dto.getPatientId());
        prescription.setDoctorId(dto.getDoctorId());
        prescription.setMedicalRecordId(dto.getMedicalRecordId());
        
        if (dto.getItems() != null) {
            prescription.setItems(dto.getItems().stream().map(itemDTO -> {
                Prescription.PrescriptionItem item = new Prescription.PrescriptionItem();
                item.setMedicineId(itemDTO.getMedicineId());
                item.setQuantity(itemDTO.getQuantity());
                item.setDosage(itemDTO.getDosage());
                item.setInstructions(itemDTO.getInstructions());
                return item;
            }).collect(Collectors.toList()));
        }
        
        return prescription;
    }
}
