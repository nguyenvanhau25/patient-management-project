package com.pm.authservice.interfaces;

import com.pm.authservice.application.dto.LoginRequestDTO;
import com.pm.authservice.application.dto.AuthResponse;
import com.pm.authservice.application.service.AuthService;
import com.pm.authservice.application.service.RefreshTokenService;
import com.pm.authservice.application.service.UserService;
import com.pm.authservice.domain.RefreshToken;
import com.pm.authservice.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    @Operation(summary = "Login & generate access + refresh token")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequestDTO request) {

        Optional<AuthResponse> authResponse = authService.authenticate(request);

        return authResponse
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401).build());
    }

    @PostMapping("/signup")
    @Operation(summary = "Register user")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {

        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(409).build();
        }

        userService.createUser(user);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Validate Access Token & return role")
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        Map<String, Object> response = new HashMap<>();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.put("status", HttpStatus.UNAUTHORIZED.value());
            response.put("error", "Authorization header missing or invalid");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = authHeader.substring(7);
        String role = authService.validateToken(token);

        if (role == null) {
            response.put("status", HttpStatus.UNAUTHORIZED.value());
            response.put("error", "Invalid token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        response.put("status", HttpStatus.OK.value());
        response.put("message", "Token is valid");
        response.put("role", role);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Generate new access token using refresh token")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {

        Optional<RefreshToken> tokenEntity = refreshTokenService.findByToken(refreshToken);

        if (tokenEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid refresh token");
        }

        Optional<String> newAccessToken = authService.refreshAccessToken(refreshToken);

        if (newAccessToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Refresh token expired or invalid");
        }

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken.get()));
    }
    //user đang đăng nhập trên laptop và điện thoại, xóa token laptop thì điện thoại vẫn còn dùng được.
    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String refreshToken) {
        boolean deleted = refreshTokenService.deleteByToken(refreshToken);
        if (deleted) {
            return ResponseEntity.ok("Refresh token deleted successfully");
        }
        return ResponseEntity.status(404).body("Refresh token not found");
    }
    @DeleteMapping("/logout/all")
    public ResponseEntity<String> logoutAll(@RequestParam String email) {
        boolean deleted = refreshTokenService.deleteByEmail(email);
        if (deleted) {
            return ResponseEntity.ok("All refresh tokens deleted for user: " + email);
        }
        return ResponseEntity.status(404).body("No refresh tokens found for user: " + email);
    }


    }

