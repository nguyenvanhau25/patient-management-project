package com.pm.authservice.infrastructure.exception;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException() {
        super("Token không hợp lệ hoặc đã hết hạn.");
    }
}
