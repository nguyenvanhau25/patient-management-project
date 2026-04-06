package com.pm.authservice.infrastructure.exception;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException(String token) {
        super("Không tìm thấy refresh token: " + token);
    }
}
