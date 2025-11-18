package com.pm.authservice.application.service;

import com.pm.authservice.domain.RefreshToken;
import com.pm.authservice.domain.User;
import com.pm.authservice.infrastructure.repo.RefreshTokenRepository;
import com.pm.authservice.infrastructure.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;  // dùng để tạo JWT


    private final long refreshTokenDurationMs = 7 * 24 * 60 * 60 * 1000L; // 7 ngày

    public RefreshToken createRefreshToken(String email, String role) {
        String token = jwtUtil.generateToken(email, role); // tạo JWT chứa email + role
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .userEmail(email)
                .role(role)
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    // kiểm tra token có còn hạn hay không
    public Optional<RefreshToken> verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            return Optional.empty();
        }
        return Optional.of(token);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    // Xóa token theo giá trị token
    public boolean deleteByToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if (refreshToken.isPresent()) {
            refreshTokenRepository.deleteByToken(token);
            return true;
        }
        return false;
    }
    public boolean deleteByEmail(String email) {
        int deletedCount = refreshTokenRepository.deleteByUserEmail(email);
        return deletedCount > 0; // true nếu xóa ít nhất 1 bản ghi
    }

}

