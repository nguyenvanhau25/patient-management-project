package com.pm.authservice.interfaces;

import com.pm.authservice.application.dto.LoginRequestDTO;
import com.pm.authservice.application.dto.AuthResponse;
import com.pm.authservice.application.service.AuthService;
import com.pm.authservice.application.service.RefreshTokenService;
import com.pm.authservice.application.service.UserService;
import com.pm.authservice.domain.RefreshToken;
import com.pm.authservice.domain.User;
import com.pm.authservice.infrastructure.exception.RefreshTokenNotFoundException;
import com.pm.authservice.infrastructure.exception.UserAlreadyExistsException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;


    @PostMapping("/login")
    @Operation(summary = "Đăng nhập & tạo access + refresh token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequestDTO request) {

        Optional<AuthResponse> authResponse = authService.authenticate(request);

        return authResponse
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401).build());
    }


    @PostMapping("/signup")
    @Operation(summary = "Đăng ký người dùng")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {

        if (userService.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail());
        }


        userService.createUser(user);
        return ResponseEntity.ok(user);
    }

    //  kiểm tra token
    @Operation(summary = "Xác thực Access Token & trả về quyền (role)")
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        Map<String, Object> response = new HashMap<>();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.put("status", HttpStatus.UNAUTHORIZED.value());
            response.put("error", "Thiếu hoặc sai định dạng header Authorization");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = authHeader.substring(7);
        String role = authService.validateToken(token);

        if (role == null) {
            response.put("status", HttpStatus.UNAUTHORIZED.value());
            response.put("error", "Token không hợp lệ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        response.put("status", HttpStatus.OK.value());
        response.put("message", "Token hợp lệ");
        response.put("role", role);
        return ResponseEntity.ok(response);
    }

    // refresh token
    @PostMapping("/refresh")
    @Operation(summary = "Tạo access token mới bằng refresh token")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {

        Optional<RefreshToken> tokenEntity = refreshTokenService.findByToken(refreshToken);

        if (tokenEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Refresh token đã hết hạn hoặc không hợp lệ");
        }

        return ResponseEntity.ok(Map.of("accessToken", tokenEntity.get()));
    }
    @DeleteMapping("/logout")
    @Operation(summary = "đăng xuất")
    public ResponseEntity<String> logout(@RequestParam String refreshToken) {
        boolean deleted = refreshTokenService.deleteByToken(refreshToken);
        if (!deleted) throw new RefreshTokenNotFoundException(refreshToken);
        return ResponseEntity.ok("Xóa refresh token thành công");
    }

    // 6 đăng xuất tất cả
    @DeleteMapping("/logout/all")
    @Operation(summary = "đăng xuất tất cả")
    public ResponseEntity<String> logoutAll(@RequestParam String email) {
        boolean deleted = refreshTokenService.deleteByEmail(email);
        if (deleted) {
            return ResponseEntity.ok("Đã xóa tất cả refresh token của người dùng: " + email);
        }
        return ResponseEntity.status(404).body("Không tìm thấy refresh token nào của người dùng: " + email);
    }

    // 7 reset/change password
    @PostMapping("/reset")
    @Operation(summary = "reset/change password ")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody LoginRequestDTO change) {
        boolean reset = userService.resetPassword(change);
        if (reset) {
            return ResponseEntity.ok("Đặt lại mật khẩu thành công");
        }
        return ResponseEntity.status(404).body("Đặt lại mật khẩu thất bại");
    }

    // 8 xem tất cả danh sách user
    @GetMapping("/user")
    @Operation(summary = "xem danh sách user")
    public ResponseEntity<List<User>> getAll(){
        return ResponseEntity.ok(userService.getAll());
    }

    }

