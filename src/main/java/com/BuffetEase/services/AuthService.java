package com.BuffetEase.services;

import com.BuffetEase.dtos.*;
import com.BuffetEase.exceptions.*;
import com.BuffetEase.entities.RefreshTokenEntity;
import com.BuffetEase.entities.UserEntity;
import com.BuffetEase.repositories.RefreshTokenRepository;
import com.BuffetEase.repositories.UserRepository;
import com.BuffetEase.utils.OtpStore;
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
import com.BuffetEase.exceptions.InvalidOtpException;
import com.BuffetEase.repositories.UserCredentialRepository;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserCredentialRepository userCredentialRepository;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            UserCredentialRepository userCredentialRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userCredentialRepository=userCredentialRepository;
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

            String subject = "New Login Alert";
            String body = "Hello " + user.getName() + ",\n\n" +
                    "You just logged in at " + LocalDateTime.now() + ".\n" +
                    "If this wasn't you, please contact support immediately.";
            emailService.sendEmail(user.getEmail(), subject, body);

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

    public void sendResetOtp(String email) {

        userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        OtpStore.storeOtp(email, otp);

        emailService.sendEmail(
                email,
                "Password Reset OTP",
                "Your OTP is: " + otp + "\nValid for 5 minutes."
        );
    }

    public void resetPassword(ResetPasswordDTO dto) {

        OtpStore.OtpData otpData = OtpStore.getOtp(dto.getEmail());

        if (otpData == null) {
            throw new InvalidOtpException("OTP not found");
        }

        if (otpData.expiry.isBefore(LocalDateTime.now())) {
            OtpStore.removeOtp(dto.getEmail());
            throw new InvalidOtpException("OTP expired");
        }

        if (!otpData.otp.equals(dto.getOtp())) {
            throw new InvalidOtpException("Invalid OTP");
        }

        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());
        userCredentialRepository.updatePassword(dto.getEmail(), encodedPassword);

        OtpStore.removeOtp(dto.getEmail());
    }

    public void changePassword(UserEntity user, ChangePasswordDTO dto) {

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Old password incorrect");
        }

        String encoded = passwordEncoder.encode(dto.getNewPassword());
        userCredentialRepository.updatePassword(user.getEmail(), encoded);
    }



}