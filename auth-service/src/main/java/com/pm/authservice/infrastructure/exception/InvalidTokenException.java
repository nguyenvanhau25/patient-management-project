package com.pm.authservice.infrastructure.exception;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException() {
        super("Invalid or expired token.");
    }
}
