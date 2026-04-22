package com.pm.clinicalservice.application.service;

import com.pm.clinicalservice.application.dto.ChatMessageDTO;
import com.pm.clinicalservice.application.dto.ChatbotRequestDTO;
import com.pm.clinicalservice.application.dto.ChatbotResponseDTO;
import com.pm.clinicalservice.interfaces.client.OpenAIClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIChatbotService {
    private final OpenAIClient openAIClient;

    public ChatbotResponseDTO getChatbotResponse(ChatbotRequestDTO request) {
        if (!openAIClient.isConfigured()) {
            return new ChatbotResponseDTO(
                    "Xin lỗi, hệ thống AI hiện chưa được cấu hình. Vui lòng thử lại sau hoặc liên hệ Hotline.");
        }

        List<Map<String, String>> messages = new ArrayList<>();
        // System prompt
        messages.add(Map.of("role", "system", "content",
                "Bạn là Hậu Anh AI Assistant, trợ lý ảo y khoa thông minh của Bệnh viện Hậu Anh (Hauuahh Hospital). " +
                        "Nhiệm vụ của bạn là tư vấn các dịch vụ bệnh viện, hỗ trợ đặt lịch khám, cung cấp thông tin chung về chuyên khoa, "
                        +
                        "và giải đáp thắc mắc cơ bản về sức khỏe. " +
                        "Hãy trả lời một cách lịch sự, thân thiện, ngắn gọn và hữu ích. Dùng tiếng Việt chuẩn mực. " +
                        "Tuyệt đối KHÔNG trả lời các câu hỏi về chính trị, tôn giáo, hay các vấn đề không liên quan y tế. "
                        +
                        "Tuyệt đối KHÔNG đưa ra phác đồ điều trị chính thức hay kê đơn thuốc (khuyên bệnh nhân nên đến khám để được bác sĩ tư vấn chi tiết)."));

        if (request.getHistory() != null) {
            for (ChatMessageDTO msg : request.getHistory()) {
                messages.add(Map.of("role", msg.getRole(), "content", msg.getContent()));
            }
        }

        messages.add(Map.of("role", "user", "content", request.getMessage()));

        try {
            String aiResponse = openAIClient.generateChatResponse(messages);
            return new ChatbotResponseDTO(aiResponse);
        } catch (Exception ex) {
            return new ChatbotResponseDTO(
                    "Xin lỗi, tôi đang gặp khó khăn trong việc kết nối hệ thống AI. Bạn vui lòng thử lại sau một lát.");
        }
    }
}
