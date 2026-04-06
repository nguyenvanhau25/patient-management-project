package com.pm.authservice.infrastructure.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String email) {
        super("Người dùng với email " + email + " đã tồn tại.");
    }
}
