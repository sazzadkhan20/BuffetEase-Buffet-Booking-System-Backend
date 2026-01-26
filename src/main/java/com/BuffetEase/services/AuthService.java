package com.BuffetEase.services;

import com.BuffetEase.dtos.LoginRequestDTO;
import com.BuffetEase.dtos.LoginResponseDTO;
import com.BuffetEase.dtos.RegisterDTO;
import com.BuffetEase.exceptions.*;
import com.BuffetEase.entities.RefreshTokenEntity;
import com.BuffetEase.entities.UserEntity;
import com.BuffetEase.repositories.RefreshTokenRepository;
import com.BuffetEase.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean register(RegisterDTO registerDTO)
    {
        registerDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        return this.userRepository.register(registerDTO);
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            UserEntity user = (UserEntity) authentication.getPrincipal();

            String accessToken = jwtService.generateAccessToken(user);
            String refreshTokenString = UUID.randomUUID().toString();

//            RefreshTokenEntity refreshToken = new RefreshTokenEntity(
//                    refreshTokenString,
//                    user.getId(),
//                    LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000)
//            );
//
//            refreshTokenRepository.deleteByUserId(user.getId());
//            refreshTokenRepository.save(refreshToken);
            userRepository.updateLastLogin(user.getEmail());
            return new LoginResponseDTO(
                    accessToken,
                    refreshTokenString,
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getRoleName()
            );

        } catch (AuthenticationException e)
        {
            //System.out.println("Asi");
            throw new InvalidCredentialsException("Invalid email or password");
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