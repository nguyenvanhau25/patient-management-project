package com.pm.analyticsservice.infrastructure.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
    private String code;
    private String message;
    private T result;
}
