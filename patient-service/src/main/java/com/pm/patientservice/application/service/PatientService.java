package com.pm.patientservice.application.service;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.pm.patientservice.application.dto.PatientRequestDTO;
import com.pm.patientservice.application.dto.PatientResponseDTO;
import com.pm.patientservice.application.exception.EmailAlreadyExistsException;
import com.pm.patientservice.application.exception.PatientNotFoundException;
import com.pm.patientservice.application.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.application.mapper.PatientMapper;
import com.pm.patientservice.domain.Patient;
import com.pm.patientservice.infrastructure.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {

  private final PatientRepository patientRepository;
  private final BillingServiceGrpcClient billingServiceGrpcClient;


  public List<PatientResponseDTO> getPatients() {
    List<Patient> patients = patientRepository.findAll();
    return patients.stream().map(PatientMapper::toDTO).toList();
  }

  public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
    if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
      throw new EmailAlreadyExistsException(
          "A patient with this email " + "already exists"
              + patientRequestDTO.getEmail());
    }
// khi tao patient thi se tao luon 1 cai tài khoản thanh toán
    Patient newPatient = patientRepository.save(
        PatientMapper.toModel(patientRequestDTO));

  billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(),
           newPatient.getName(), newPatient.getEmail()) ;
    return PatientMapper.toDTO(newPatient);
  }

  public PatientResponseDTO updatePatient(UUID id,
      PatientRequestDTO patientRequestDTO) {

    Patient patient = patientRepository.findById(id).orElseThrow(
        () -> new PatientNotFoundException("Patient not found with ID: " + id));

    if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),
        id)) {
      throw new EmailAlreadyExistsException(
          "A patient with this email " + "already exists"
              + patientRequestDTO.getEmail());
    }

    patient.setName(patientRequestDTO.getName());
    patient.setAddress(patientRequestDTO.getAddress());
    patient.setEmail(patientRequestDTO.getEmail());
    patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

    Patient updatedPatient = patientRepository.save(patient);
    return PatientMapper.toDTO(updatedPatient);
  }

  public void deletePatient(UUID id) {

    patientRepository.deleteById(id);
    try{
      billingServiceGrpcClient.deleteBillingAccount(id.toString());
    }catch (Exception e){
      throw new PatientNotFoundException("Patient not found with ID: " + id);
    }
  }

  public boolean existsById(UUID id) {
    return patientRepository.existsById(id);
  }
  public PatientResponseDTO getPatientById(UUID id) {
    Patient patient = patientRepository.findById(id).orElseThrow();
    return PatientMapper.toDTO(patient);
  }

}
