    package com.pm.authservice.interfaces;

    import com.pm.authservice.application.dto.LoginRequestDTO;
    import com.pm.authservice.application.dto.LoginResponseDTO;
    import com.pm.authservice.application.service.AuthService;
    import io.swagger.v3.oas.annotations.Operation;
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

        @PostMapping("/login")
        @Operation(summary = "api for generate token on login user")
        public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {

            Optional<String> token = authService.authenticate(loginRequestDTO);

            if(token.isEmpty()){
                return ResponseEntity.status(401).build();
            }
            return ResponseEntity.ok(new LoginResponseDTO(token.get()));
        }
        @Operation(summary = "Validate Token")
        @GetMapping("/validate")
        public ResponseEntity<Map<String, Object>> validateToken(
                @RequestHeader(value = "Authorization", required = false) String authHeader) {

            Map<String, Object> response = new HashMap<>();

            try {
                // Kiểm tra header
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    response.put("status", HttpStatus.UNAUTHORIZED.value());
                    response.put("error", "Authorization header missing or invalid");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
                // lấy token thực tế
                String token = authHeader.substring(7);
                // xác thực va lâys role
                String roleAndValid = authService.validateToken(token);

                //  Token hợp lệ → trả kết quả
                response.put("status", HttpStatus.OK.value());
                response.put("message", "Token is valid");
                response.put("role", roleAndValid);

                return ResponseEntity.ok(response);
            } catch (Exception e) {
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.put("error", "An error occurred while validating the token");
                response.put("details", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }


    }
