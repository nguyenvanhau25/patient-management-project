package com.pm.authservice.infrastructure.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String email) {
        super("Không tìm thấy người dùng với email: " + email);
    }
}
