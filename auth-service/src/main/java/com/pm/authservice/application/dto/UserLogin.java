package com.pm.authservice.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLogin {
    private String email;
    private String password;
}
