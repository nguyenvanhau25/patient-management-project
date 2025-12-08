package com.pm.appointmentservice.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    APPOINTMENT_NOT_FOUND("APPOINTMENT_NOT_FOUND", "Không tìm thấy lịch hẹn", HttpStatus.NOT_FOUND),
    INVALID_DATE_FORMAT("INVALID_DATE_FORMAT", "Định dạng ngày không hợp lệ, sử dụng yyyy-MM-ddTHH:mm:ss", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR("INTERNAL_ERROR", "Lỗi hệ thống, vui lòng thử lại sau", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus statusCode;

}
