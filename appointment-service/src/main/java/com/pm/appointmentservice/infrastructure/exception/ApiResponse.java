package com.pm.appointmentservice.infrastructure.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse <T>{
    private String code;
    private String message;
    private T result;
}
