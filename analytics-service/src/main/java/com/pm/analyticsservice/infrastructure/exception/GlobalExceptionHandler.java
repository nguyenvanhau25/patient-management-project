package com.pm.analyticsservice.infrastructure.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Bắt AppException
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<?>> handleAppException(AppException ex) {
        return ResponseEntity
                .status(ex.getErrorCode().getStatusCode())
                .body(ApiResponse.builder()
                        .code(ex.getErrorCode().getCode())
                        .message(ex.getMessage())
                        .build());
    }

    // Bắt lỗi validation (nếu dùng @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.builder()
                        .code("VALIDATION_ERROR")
                        .message("Lỗi dữ liệu đầu vào")
                        .details(errors)
                        .build());
    }

    // Bắt tất cả lỗi còn lại
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
        return ResponseEntity
                .status(500)
                .body(ApiResponse.builder()
                        .code(ErrorCode.INTERNAL_ERROR.getCode())
                        .message(ex.getMessage())
                        .build());
    }
}
