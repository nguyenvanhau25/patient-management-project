package com.pm.authservice.infrastructure.exception;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException(String token) {
        super("Refresh token not found: " + token);
    }
}
