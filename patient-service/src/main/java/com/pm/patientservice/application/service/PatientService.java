package com.pm.patientservice.application.service;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.pm.patientservice.application.dto.PatientRequestDTO;
import com.pm.patientservice.application.dto.PatientResponseDTO;
import com.pm.patientservice.infrastructure.exception.EmailAlreadyExistsException;
import com.pm.patientservice.infrastructure.exception.PatientNotFoundException;
import com.pm.patientservice.infrastructure.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.infrastructure.kafka.KafkaProducer;
import com.pm.patientservice.application.mapper.PatientMapper;
import com.pm.patientservice.domain.Patient;
import com.pm.patientservice.infrastructure.repo.PatientRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {

  private final PatientRepository patientRepository;
  private final BillingServiceGrpcClient billingServiceGrpcClient;
  private final KafkaProducer kafkaProducer;

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

    Patient newPatient = patientRepository.save(
            PatientMapper.toModel(patientRequestDTO));
// khi tao patient thi se tao luon 1 cai tài khoản thanh toán
    billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(),
            newPatient.getName(), newPatient.getEmail());

    kafkaProducer.sendEvent(newPatient);

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

  // xóa patient xóa luôn cả tài khoản thanh toán
  public void deletePatient(UUID id) {

    patientRepository.deleteById(id);
    try {
      billingServiceGrpcClient.deleteBillingAccount(id.toString());
    } catch (Exception e) {
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

  // xuất file pdf
  public void exportPatientPdf(HttpServletResponse response) throws Exception {
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=patients_report.pdf");

    List<Patient> patients = patientRepository.findAll();

    com.lowagie.text.Document document = new com.lowagie.text.Document();
    PdfWriter.getInstance(document, response.getOutputStream());

    document.open();

    com.lowagie.text.Paragraph title = new com.lowagie.text.Paragraph("Patient Report");
    title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
    document.add(title);

    PdfPTable table = new PdfPTable(4);
    table.addCell("ID");
    table.addCell("Name");
    table.addCell("Email");
    table.addCell("Date of Birth");
    table.addCell("Address");
    table.addCell("Created Date");

    for (Patient p : patients) {
      table.addCell(p.getId().toString());
      table.addCell(p.getName());
      table.addCell(p.getEmail());
      table.addCell(p.getDateOfBirth().toString());
      table.addCell(p.getAddress());
      table.addCell(p.getRegisteredDate().toString());
    }

    document.add(table);
    document.close();
  }
}
