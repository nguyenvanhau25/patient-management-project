package com.pm.billingservice.infrastructure.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Common errors
    INTERNAL_ERROR("INTERNAL_ERROR", "Lỗi hệ thống, vui lòng thử lại sau", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT("INVALID_INPUT", "Dữ liệu đầu vào không hợp lệ", HttpStatus.BAD_REQUEST),
    NOT_FOUND("NOT_FOUND", "Không tìm thấy tài nguyên", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("UNAUTHORIZED", "Không có quyền truy cập", HttpStatus.UNAUTHORIZED),

    // BillingAccount errors
    BILLING_ACCOUNT_NOT_FOUND("BILLING_ACCOUNT_NOT_FOUND", "Không tìm thấy BillingAccount", HttpStatus.NOT_FOUND),
    BILLING_ACCOUNT_ALREADY_EXISTS("BILLING_ACCOUNT_ALREADY_EXISTS", "BillingAccount đã tồn tại", HttpStatus.CONFLICT),
    BILLING_ACCOUNT_INVALID_STATUS("BILLING_ACCOUNT_INVALID_STATUS", "Trạng thái BillingAccount không hợp lệ", HttpStatus.BAD_REQUEST),

    // BillingTransaction errors
    TRANSACTION_NOT_FOUND("TRANSACTION_NOT_FOUND", "Không tìm thấy giao dịch", HttpStatus.NOT_FOUND),
    TRANSACTION_INVALID_AMOUNT("TRANSACTION_INVALID_AMOUNT", "Số tiền giao dịch không hợp lệ", HttpStatus.BAD_REQUEST),
    TRANSACTION_INVALID_STATUS("TRANSACTION_INVALID_STATUS", "Trạng thái giao dịch không hợp lệ", HttpStatus.BAD_REQUEST),

    // Appointment errors
    APPOINTMENT_NOT_FOUND("APPOINTMENT_NOT_FOUND", "Không tìm thấy lịch hẹn", HttpStatus.NOT_FOUND),
    APPOINTMENT_INVALID_STATUS("APPOINTMENT_INVALID_STATUS", "Trạng thái lịch hẹn không hợp lệ", HttpStatus.BAD_REQUEST),
    APPOINTMENT_TIME_CONFLICT("APPOINTMENT_TIME_CONFLICT", "Thời gian lịch hẹn bị trùng", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus statusCode;

    ErrorCode(String code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
