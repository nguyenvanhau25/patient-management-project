package com.pm.patientservice.infrastructure.exception;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(
      GlobalExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidationException(
      MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(
        error -> errors.put(error.getField(), error.getDefaultMessage()));

    return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
            .code("VALIDATION_ERROR")
            .message("Lỗi dữ liệu đầu vào")
            .details(errors)
            .build());
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ApiResponse<Void>> handleEmailAlreadyExistsException(
      EmailAlreadyExistsException ex) {

    log.warn("Email address already exist {}", ex.getMessage());
    return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
            .code("EMAIL_EXISTS")
            .message("Email address already exists")
            .build());
  }

  @ExceptionHandler(PatientNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handlePatientNotFoundException(
      PatientNotFoundException ex) {
    log.warn("Patient not found {}", ex.getMessage());

    return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
            .code("PATIENT_NOT_FOUND")
            .message("Patient not found")
            .build());
  }
}
