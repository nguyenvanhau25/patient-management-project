package com.pm.patientservice.application.service;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.pm.patientservice.application.dto.PatientRequestDTO;
import com.pm.patientservice.application.dto.PatientResponseDTO;
import com.pm.patientservice.domain.repository.PatientDomainRepository;
import com.pm.patientservice.domain.service.PatientDomainService;
import com.pm.patientservice.infrastructure.exception.PatientNotFoundException;
import com.pm.patientservice.infrastructure.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.infrastructure.kafka.KafkaProducer;
import com.pm.patientservice.application.mapper.PatientMapper;
import com.pm.patientservice.domain.model.Patient;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {

  private final PatientDomainRepository patientRepo;
  private final PatientDomainService domainService;
  private final BillingServiceGrpcClient billingServiceGrpcClient;
  private final KafkaProducer kafkaProducer;
  private final FileStorageService fileStorageService;

  /**
   * Tải lên và cập nhật ảnh đại diện cho bệnh nhân từ tệp vật lý.
   */
  public void uploadImage(UUID id, org.springframework.web.multipart.MultipartFile file) {
    Patient patient = patientRepo.findById(id)
            .orElseThrow(() -> new PatientNotFoundException("Không tìm thấy bệnh nhân với ID: " + id));
    String fileName = fileStorageService.storeFile(file);
    // Ánh xạ đến URL có thể phục vụ (ví dụ: /api/patients/uploads/filename)
    String fileUrl = "/api/patients/uploads/" + fileName;
    patient.updateProfileImage(fileUrl);
    patientRepo.save(patient);
  }

  public List<PatientResponseDTO> getPatients() {
    return patientRepo.findAll()
            .stream()
            .map(PatientMapper::toDTO)
            .toList();
  }

  public PatientResponseDTO createPatient(PatientRequestDTO dto) {

    domainService.ensureEmailNotExists(
            patientRepo.existsByEmail(dto.getEmail()),
            dto.getEmail()
    );

    Patient patient = Patient.register(
            dto.getName(),
            dto.getEmail(),
            dto.getAddress(),
            LocalDate.parse(dto.getDateOfBirth())
    );

    Patient saved = patientRepo.save(patient);
    if (dto.getProfileImageUrl() != null && !dto.getProfileImageUrl().isBlank()) {
      saved.updateProfileImage(dto.getProfileImageUrl());
      saved = patientRepo.save(saved);
    }

    // khi tao patient thi se tao luon 1 cai tài khoản thanh toán
    billingServiceGrpcClient.createBillingAccount(saved.getId().toString(),
            saved.getName(), saved.getEmail());

    kafkaProducer.sendEvent(saved);

    return PatientMapper.toDTO(saved);
  }


  public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO dto) {

    Patient patient = patientRepo.findById(id)
            .orElseThrow(() ->
                    new PatientNotFoundException("Không tìm thấy bệnh nhân với ID: " + id));

    domainService.ensureEmailNotExists(
            patientRepo.existsByEmailAndNotId(dto.getEmail(), id),
            dto.getEmail()
    );

    patient.updateProfile(
            dto.getName(),
            dto.getEmail(),
            dto.getAddress(),
            LocalDate.parse(dto.getDateOfBirth())
    );
    if (dto.getProfileImageUrl() != null && !dto.getProfileImageUrl().isBlank()) {
      patient.updateProfileImage(dto.getProfileImageUrl());
    }

    return PatientMapper.toDTO(patientRepo.save(patient));
  }


  public void deletePatient(UUID id) {

    if (!patientRepo.existsById(id)) {
      throw new PatientNotFoundException("Không tìm thấy bệnh nhân với ID: " + id);
    }

    patientRepo.deleteById(id);

    billingServiceGrpcClient.deleteBillingAccount(id.toString());
  }


  public PatientResponseDTO getPatientById(UUID id) {
    Patient patient = patientRepo.findById(id)
            .orElseThrow(() ->
                    new PatientNotFoundException("Không tìm thấy bệnh nhân với ID: " + id));
    return PatientMapper.toDTO(patient);
  }


  public boolean existsById(UUID id) {
    return patientRepo.existsById(id);
  }

  public PatientResponseDTO updatePatientImage(UUID id, String profileImageUrl) {
    Patient patient = patientRepo.findById(id)
            .orElseThrow(() -> new PatientNotFoundException("Không tìm thấy bệnh nhân với ID: " + id));
    patient.updateProfileImage(profileImageUrl);
    return PatientMapper.toDTO(patientRepo.save(patient));
  }

  // xuất file pdf
  public void exportPatientPdf(HttpServletResponse response) throws Exception {
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=patients_report.pdf");

    List<Patient> patients = patientRepo.findAll();

    com.lowagie.text.Document document = new com.lowagie.text.Document();
    PdfWriter.getInstance(document, response.getOutputStream());

    document.open();

    com.lowagie.text.Paragraph title = new com.lowagie.text.Paragraph("Báo cáo danh sách bệnh nhân");
    title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
    document.add(title);

    PdfPTable table = new PdfPTable(4);
    table.addCell("ID");
    table.addCell("Tên");
    table.addCell("Email");
    table.addCell("Ngày sinh");
    table.addCell("Địa chỉ");
    table.addCell("Ngày đăng ký");

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
