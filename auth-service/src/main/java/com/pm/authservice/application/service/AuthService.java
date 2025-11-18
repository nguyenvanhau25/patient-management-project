package com.pm.authservice.application.service;

import com.pm.authservice.application.dto.LoginRequestDTO;
import com.pm.authservice.application.dto.AuthResponse;
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
    private final RefreshTokenService refreshTokenService;


    public Optional<AuthResponse> authenticate(LoginRequestDTO loginRequestDTO) {
        return userService
                .findByEmail(loginRequestDTO.getEmail())
                .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword(), u.getPassword()))
                .map(u -> {
                    // Access Token chứa email + role
                    String accessToken = jwtUtil.generateToken(
                            u.getEmail(),
                            u.getRole().name()
                    );

                    // Refresh Token cũng chứa email + role, lưu vào DB
                    String refreshToken = refreshTokenService
                            .createRefreshToken(u.getEmail(), u.getRole().name())
                            .getToken();

                    return new AuthResponse(accessToken, refreshToken);
                });
    }

    public String validateToken(String token) {
        try {
            return jwtUtil.validateToken(token);
        } catch (JwtException e) {
            return null;
        }
    }

    public Optional<String> refreshAccessToken(String refreshToken) {

        return refreshTokenService.findByToken(refreshToken)
                .flatMap(refreshTokenService::verifyExpiration)
                .map(t -> jwtUtil.generateToken(t.getUserEmail(), t.getRole()));
    }
}
