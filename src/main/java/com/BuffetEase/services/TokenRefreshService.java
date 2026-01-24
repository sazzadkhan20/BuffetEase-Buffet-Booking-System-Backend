package com.BuffetEase.services;

import com.BuffetEase.entities.RefreshTokenEntity;
import com.BuffetEase.entities.UserEntity;
import com.BuffetEase.repositories.RefreshTokenRepository;
import com.BuffetEase.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TokenRefreshService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public TokenRefreshService(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository,
            JwtService jwtService
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public Optional<String> attemptAutoRefresh(String email) {
        try {
            Optional<UserEntity> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                System.out.println("Auto-refresh failed: User not found - " + email);
                return Optional.empty();
            }

            UserEntity user = userOpt.get();

            Optional<RefreshTokenEntity> refreshTokenOpt = refreshTokenRepository.findByUserId(user.getId());
            if (refreshTokenOpt.isEmpty()) {
                System.out.println("Auto-refresh failed: No refresh token found for user - " + email);
                return Optional.empty();
            }

            RefreshTokenEntity refreshToken = refreshTokenOpt.get();

            if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                System.out.println("Auto-refresh failed: Refresh token expired for user - " + email);
                refreshTokenRepository.deleteByUserId(user.getId());
                return Optional.empty();
            }

            String newAccessToken = jwtService.generateAccessToken(user);
            System.out.println("Auto-refresh successful for user: " + email);

            return Optional.of(newAccessToken);

        } catch (Exception e) {
            System.err.println("Auto-refresh error: " + e.getMessage());
            return Optional.empty();
        }
    }
}