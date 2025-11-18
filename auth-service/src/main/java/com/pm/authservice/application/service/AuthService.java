package com.pm.authservice.application.service;

import com.pm.authservice.application.dto.LoginRequestDTO;
import com.pm.authservice.infrastructure.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
        Optional<String > token = userService
                .findByEmail(loginRequestDTO.getEmail())
                .filter(u -> passwordEncoder
                        .matches(loginRequestDTO.getPassword(),u.getPassword()))
                .map(u -> jwtUtil.generateToken(u.getEmail(), u.getRole().name()));
        return token;
    }

    //  mật khẩu nhập với mật khẩu user.getpass trong db được so sánh bằng encoder
//  sau khi xác nhân đúng thì sẽ dùng jwtutil
//  để tạo token với email là subject, role là phần payload, kí bằng secretkey
    public String validateToken(String token) {
        try {
            return jwtUtil.validateToken(token);
        } catch (JwtException e){
            return null;
        }
    }
}
