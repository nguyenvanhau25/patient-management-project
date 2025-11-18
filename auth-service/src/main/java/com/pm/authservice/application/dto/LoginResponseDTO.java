package com.pm.authservice.application.dto;

import lombok.Getter;

@Getter
public class LoginResponseDTO {
    // trả ra 1 token để có thể đăng nhập
    private final String token;
    public LoginResponseDTO(String token) {
        this.token = token;
    }
}
