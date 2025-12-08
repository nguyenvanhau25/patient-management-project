package com.pm.authservice.infrastructure.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String email) {
        super("User with email " + email + " not found.");
    }
}
