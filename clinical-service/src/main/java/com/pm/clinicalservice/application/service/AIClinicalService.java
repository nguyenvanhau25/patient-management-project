package com.pm.clinicalservice.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.clinicalservice.application.dto.AIClinicalRequestDTO;
import com.pm.clinicalservice.application.dto.AIClinicalResponseDTO;
import com.pm.clinicalservice.application.dto.OpenAIClinicalPayload;
import com.pm.clinicalservice.domain.model.MedicalRecord;
import com.pm.clinicalservice.domain.repository.MedicalRecordRepository;
import com.pm.clinicalservice.interfaces.client.DoctorClient;
import com.pm.clinicalservice.interfaces.client.OpenAIClient;
import com.pm.clinicalservice.interfaces.client.PatientClient;
import com.pm.clinicalservice.interfaces.client.dto.DoctorSummaryDTO;
import com.pm.clinicalservice.interfaces.client.dto.PatientSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIClinicalService {
    private static final String DISCLAIMER = "Kết quả AI chỉ mang tính hỗ trợ tham khảo, không thay thế chẩn đoán, chỉ định và quyết định điều trị lâm sàng chính thức của bác sĩ.";

    private final PatientClient patientClient;
    private final DoctorClient doctorClient;
    private final MedicalRecordRepository medicalRecordRepository;
    private final OpenAIClient openAIClient;
    private final ObjectMapper objectMapper;

    public AIClinicalResponseDTO generateDiagnosisTemplate(AIClinicalRequestDTO request) {
        validateDependencies(request.getPatientId(), request.getDoctorId());

        PatientSummaryDTO patient = patientClient.getPatientDetail(request.getPatientId());
        DoctorSummaryDTO doctor = doctorClient.getDoctorDetail(request.getDoctorId());
        List<MedicalRecord> records = medicalRecordRepository.findByPatientId(request.getPatientId());

        if (!openAIClient.isConfigured()) {
            return buildFallbackTemplate(request, patient, doctor, records);
        }

        String responseContent = openAIClient.generateClinicalSuggestion(
                buildSystemPrompt(),
                buildUserPrompt(request, patient, doctor, records));

        try {
            OpenAIClinicalPayload payload = objectMapper.readValue(responseContent, OpenAIClinicalPayload.class);
            return AIClinicalResponseDTO.builder()
                    .patientId(patient.getId())
                    .patientName(patient.getName())
                    .doctorId(doctor.getId())
                    .doctorName(doctor.getName())
                    .specialty(doctor.getSpecialization())
                    .clinicalSummary(defaultIfBlank(payload.getClinicalSummary(), request.getChiefComplaint()))
                    .suggestedDiagnosis(defaultIfBlank(payload.getSuggestedDiagnosis(), "Cần đánh giá bổ sung"))
                    .riskLevel(defaultIfBlank(payload.getRiskLevel(), "TRUNG_BINH"))
                    .recommendedActions(normalizeList(payload.getRecommendedActions(),
                            List.of("Khám lâm sàng trực tiếp và đối chiếu cận lâm sàng")))
                    .redFlags(normalizeList(payload.getRedFlags(),
                            List.of("Cần xác nhận lại bằng thăm khám và xét nghiệm phù hợp")))
                    .disclaimer(DISCLAIMER)
                    .historicalRecordCount(records.size())
                    .aiGenerated(true)
                    .build();
        } catch (Exception ex) {
            return buildFallbackTemplate(request, patient, doctor, records);
        }
    }

    private void validateDependencies(UUID patientId, UUID doctorId) {
        if (patientId == null || !patientClient.checkPatientExists(patientId)) {
            throw new RuntimeException("Không tìm thấy bệnh nhân: " + patientId);
        }
        if (doctorId == null || !doctorClient.checkDoctorExists(doctorId)) {
            throw new RuntimeException("Không tìm thấy bác sĩ: " + doctorId);
        }
    }

    private String buildSystemPrompt() {
        return """
                Bạn là trợ lý AI y khoa chuyên nghiệp hỗ trợ lâm sàng cho bác sĩ.
                Nhiệm vụ: Phân tích thông tin bệnh nhân, triệu chứng hiện tại và tiền sử bệnh án để sinh ra mẫu chẩn đoán gợi ý.
                Yêu cầu BẮT BUỘC:
                1. Mọi nội dung trả lời phải bằng **Tiếng Việt có dấu (Vietnamese)** chuẩn mực, văn phong y khoa chuyên nghiệp.
                2. KHÔNG khẳng định chẩn đoán cuối cùng, chỉ đưa ra giả thuyết và gợi ý.
                3. Chỉ được trả về đúng cú pháp JSON hợp lệ với 5 trường chính xác:
                   "clinicalSummary" (Tóm tắt bệnh án), "suggestedDiagnosis" (Chẩn đoán sơ bộ gợi ý), "riskLevel" (Đánh giá nguy cơ), "recommendedActions" (Khuyến nghị cận lâm sàng/điều trị), "redFlags" (Các dấu hiệu cảnh báo đỏ nếu có).
                4. "riskLevel" CHỈ được phép mang 1 trong 3 giá trị: "THAP", "TRUNG_BINH", "CAO".
                5. "recommendedActions" và "redFlags" phải là mảng (Array) các chuỗi ngắn gọn.
                """;
    }

    private String buildUserPrompt(AIClinicalRequestDTO request,
            PatientSummaryDTO patient,
            DoctorSummaryDTO doctor,
            List<MedicalRecord> records) {
        String history = records.isEmpty()
                ? "Bệnh nhân chưa có hồ sơ bệnh án trước đó tại cơ sở."
                : records.stream()
                        .limit(5)
                        .map(record -> String.format(
                                "- Ngày khám: %s | Chẩn đoán: %s | Triệu chứng: %s | Ghi chú: %s",
                                record.getVisitDate(),
                                sanitize(record.getDiagnosis()),
                                sanitize(record.getSymptoms()),
                                sanitize(record.getNotes())))
                        .collect(Collectors.joining("\n"));

        return String.format("""
                Thông tin bệnh nhân:
                - Hồ sơ ID: %s
                - Họ tên: %s
                - Tuổi ước tính: %s
                - Email: %s
                - Địa chỉ: %s

                Bác sĩ phụ trách:
                - ID: %s
                - Họ tên: %s
                - Chuyên khoa: %s

                Các thông tin lâm sàng hiện tại:
                - Lý do đến khám (Chief Complaint): %s
                - Triệu chứng hiện tại (Current Symptoms): %s
                - Ghi chú thăm khám/Tiền sử thêm (Notes): %s

                Lịch sử bệnh án gần đây (Tối đa 5 lần khám gần nhất):
                %s
                
                Hãy tổng hợp và đưa ra đề xuất.
                """,
                patient.getId(),
                sanitize(patient.getName()),
                calculateAge(patient.getDateOfBirth()),
                sanitize(patient.getEmail()),
                sanitize(patient.getAddress()),
                doctor.getId(),
                sanitize(doctor.getName()),
                sanitize(doctor.getSpecialization()),
                sanitize(request.getChiefComplaint()),
                sanitize(request.getCurrentSymptoms()),
                sanitize(request.getNotes()),
                history);
    }

    private AIClinicalResponseDTO buildFallbackTemplate(AIClinicalRequestDTO request,
            PatientSummaryDTO patient,
            DoctorSummaryDTO doctor,
            List<MedicalRecord> records) {
        List<String> actions = new ArrayList<>();
        actions.add("Hoàn tất thăm khám lâm sàng và đánh giá dấu hiệu sinh tồn");
        actions.add("Cân nhắc chỉ định xét nghiệm hoặc chẩn đoán hình ảnh phù hợp nếu triệu chứng kéo dài");
        actions.add("Đối chiếu với tiền sử bệnh án trước khi ra phác đồ điều trị");

        List<String> redFlags = new ArrayList<>();
        redFlags.add("Nếu có dấu hiệu suy hô hấp, rối loạn tri giác hoặc đau tăng nhanh cần xử trí cấp cứu");
        if (!records.isEmpty()) {
            redFlags.add("Bệnh nhân đã có tiền sử khám trước đó, cần khảo sát lại để so sánh diễn tiến hiện tại");
        }

        String summary = "Bệnh nhân " + sanitize(patient.getName())
                + " đang được đánh giá với lý do đến khám: " + sanitize(request.getChiefComplaint()) + ".";
        if (request.getCurrentSymptoms() != null && !request.getCurrentSymptoms().isBlank()) {
            summary += " Triệu chứng hiện tại: " + request.getCurrentSymptoms().trim() + ".";
        }

        return AIClinicalResponseDTO.builder()
                .patientId(patient.getId())
                .patientName(patient.getName())
                .doctorId(doctor.getId())
                .doctorName(doctor.getName())
                .specialty(doctor.getSpecialization())
                .clinicalSummary(summary)
                .suggestedDiagnosis("Theo dõi thêm và cần khám cận lâm sàng để xác định chẩn đoán phân biệt")
                .riskLevel(records.isEmpty() ? "TRUNG_BINH" : "CAO")
                .recommendedActions(actions)
                .redFlags(redFlags)
                .disclaimer(DISCLAIMER)
                .historicalRecordCount(records.size())
                .aiGenerated(false)
                .build();
    }

    private String calculateAge(String dateOfBirth) {
        try {
            LocalDate birthDate = LocalDate.parse(dateOfBirth);
            return String.valueOf(Period.between(birthDate, LocalDate.now()).getYears());
        } catch (Exception ex) {
            return "Không rõ";
        }
    }

    private String sanitize(String value) {
        return value == null || value.isBlank() ? "Không có" : value.trim();
    }

    private String defaultIfBlank(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }

    private List<String> normalizeList(List<String> value, List<String> fallback) {
        if (value == null || value.isEmpty()) {
            return fallback;
        }
        return value.stream()
                .filter(item -> item != null && !item.isBlank())
                .map(String::trim)
                .collect(Collectors.toList());
    }
}
