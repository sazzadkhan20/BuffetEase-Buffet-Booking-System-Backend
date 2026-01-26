package com.BuffetEase.controllers;

import com.BuffetEase.dtos.*;
import com.BuffetEase.entities.UserEntity;
import com.BuffetEase.exceptions.*;
import com.BuffetEase.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO)
    {
        boolean flag = authService.register(registerDTO);
        if (flag) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "User registered successfully");
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Registration failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
            LoginResponseDTO response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        String newAccessToken = authService.refreshAccessToken(request.getRefreshToken());
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        response.put("tokenType", "Bearer");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        authService.logout(user.getId());

        return ResponseEntity.ok(new MessageResponseDTO("Logged out successfully"));
    }
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("email", user.getEmail());
        response.put("name", user.getName());
        response.put("role", user.getRoleName());

        return ResponseEntity.ok(response);
    }


    @PostMapping("/request-reset-otp")
    public ResponseEntity<?> requestOtp(@Valid @RequestBody PasswordResetRequestDTO dto) {

        authService.sendResetOtp(dto.getEmail());

        return ResponseEntity.ok(
                Map.of("status", "success", "message", "OTP sent to email")
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDTO dto) {

        authService.resetPassword(dto);

        return ResponseEntity.ok(
                Map.of("status", "success", "message", "Password reset successfully")
        );
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) auth.getPrincipal();

        authService.changePassword(user, dto);

        return ResponseEntity.ok(
                Map.of("status", "success", "message", "Password changed successfully")
        );
    }



}