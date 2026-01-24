package com.BuffetEase.services;

import com.BuffetEase.dtos.LoginRequestDTO;
import com.BuffetEase.dtos.LoginResponseDTO;
import com.BuffetEase.exceptions.*;
import com.BuffetEase.entities.RefreshTokenEntity;
import com.BuffetEase.entities.UserEntity;
import com.BuffetEase.repositories.RefreshTokenRepository;
import com.BuffetEase.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for authentication operations
 *
 * Handles:
 * 1. User login
 * 2. Token generation (access + refresh)
 * 3. Token refresh
 * 4. Logout
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Login user and generate tokens
     *
     * Process:
     * 1. Authenticate user credentials
     * 2. Load user details
     * 3. Generate access token
     * 4. Generate refresh token
     * 5. Save refresh token to database
     * 6. Update last login time
     * 7. Return both tokens
     *
     * @param loginRequest - contains email and password
     * @return LoginResponse with access token and refresh token
     */
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            // 1. Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            UserEntity user = (UserEntity) authentication.getPrincipal();

            String accessToken = jwtService.generateAccessToken(user);
            String refreshTokenString = UUID.randomUUID().toString();

            // 5. Save refresh token to database
            RefreshTokenEntity refreshToken = new RefreshTokenEntity(
                    refreshTokenString,
                    user.getId(),
                    LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000)
            );

            // Delete old refresh tokens for this user
            refreshTokenRepository.deleteByUserId(user.getId());

            // Save new refresh token
            refreshTokenRepository.save(refreshToken);

            // 6. Update last login
            userRepository.updateLastLogin(user.getEmail());

            // 7. Return response
            return new LoginResponseDTO(
                    accessToken,
                    refreshTokenString,
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getRoleName()
            );

        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid email or password");
        }
    }

    public String refreshAccessToken(String refreshTokenString) {
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(refreshTokenString)
                .orElseThrow(() -> new TokenRefreshException("Refresh token not found"));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.deleteByToken(refreshTokenString);
            throw new TokenRefreshException("Refresh token expired. Please login again.");
        }

        UserEntity user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new TokenRefreshException("User not found"));
        return jwtService.generateAccessToken(user);
    }

    public void logout(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}