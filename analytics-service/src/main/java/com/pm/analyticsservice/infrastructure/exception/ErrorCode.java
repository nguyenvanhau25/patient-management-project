package com.pm.analyticsservice.infrastructure.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_DATE_FORMAT("INVALID_DATE_FORMAT", "Ngày không hợp lệ, định dạng phải là yyyy-MM-dd", HttpStatus.BAD_REQUEST),
    PATIENT_NOT_FOUND("PATIENT_NOT_FOUND", "Không tìm thấy bệnh nhân", HttpStatus.NOT_FOUND),
    INTERNAL_ERROR("INTERNAL_ERROR", "Lỗi hệ thống, vui lòng thử lại sau", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus statusCode;

    ErrorCode(String code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
