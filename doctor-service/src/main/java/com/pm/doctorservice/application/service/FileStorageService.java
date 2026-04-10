package com.pm.doctorservice.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

/**
 * Dịch vụ xử lý lưu trữ tệp tin cục bộ cho bác sĩ.
 */
@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir:uploads}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();

        try {
            // Tạo thư mục nếu chưa tồn tại
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Không thể tạo thư mục để lưu trữ tệp tải lên.", ex);
        }
    }

    /**
     * Lưu trữ tệp tin và trả về tên tệp duy nhất.
     */
    public String storeFile(MultipartFile file) {
        // Chuẩn hóa tên tệp
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = "";
        
        try {
            if (originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            
            // Tạo tên tệp duy nhất bằng UUID
            String fileName = UUID.randomUUID().toString() + fileExtension;

            // Kiểm tra xem tên tệp có chứa ký tự không hợp lệ không
            if (fileName.contains("..")) {
                throw new RuntimeException("Xin lỗi! Tên tệp chứa chuỗi đường dẫn không hợp lệ " + fileName);
            }

            // Sao chép tệp vào vị trí đích (thay thế nếu đã tồn tại)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Không thể lưu tệp " + originalFileName + ". Vui lòng thử lại!", ex);
        }
    }
}
