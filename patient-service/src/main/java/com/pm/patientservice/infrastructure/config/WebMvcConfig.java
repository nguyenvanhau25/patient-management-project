package com.pm.patientservice.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Cấu hình để phục vụ các tệp tin tĩnh (ảnh đã tải lên).
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory(uploadDir, registry);
    }

    /**
     * Ánh xạ đường dẫn URL vào thư mục vật lý trên ổ đĩa.
     */
    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(dirName);
        String fullPath = uploadPath.toFile().getAbsolutePath();

        // Đảm bảo đường dẫn tài nguyên hợp lệ
        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/" + fullPath + "/");
    }
}
