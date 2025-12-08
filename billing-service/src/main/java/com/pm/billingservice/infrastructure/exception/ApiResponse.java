package com.pm.billingservice.infrastructure.exception;


import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String code;
    private String message;
    private int status;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .status(HttpStatus.OK.value())
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message, HttpStatus status) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .status(status.value())
                .build();
    }

}
