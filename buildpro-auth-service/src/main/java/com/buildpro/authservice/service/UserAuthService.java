package com.buildpro.authservice.service;

import com.buildpro.authservice.dto.ForgotPasswordRequest;
import com.buildpro.authservice.dto.JwtResponse;

import com.buildpro.authservice.dto.LoginRequest;
import com.buildpro.authservice.dto.RegisterRequest;
import com.buildpro.authservice.dto.ResetPasswordRequest;
import com.buildpro.authservice.entity.CustomerUser;
import com.buildpro.authservice.entity.VerificationToken;
import com.buildpro.authservice.repository.CustomerUserRepository;
import com.buildpro.authservice.repository.VerificationTokenRepository;
import com.buildpro.authservice.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final CustomerUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;

    // Step 1: Registration - save user + generate token + send email
    public ResponseEntity<String> register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        CustomerUser user = CustomerUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isEmailVerified(false)
                .build();

        userRepository.save(user);

        // Generate email verification token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();
        tokenRepository.save(verificationToken);

        // Send email
        emailService.sendVerificationEmail(user.getEmail(), token);

        return ResponseEntity.ok("Verification email sent. Please check your inbox.");
    }

    // Step 2: Verify email (triggered from email link)
    public ResponseEntity<String> verifyToken(String token) {
        Optional<VerificationToken> optionalToken = tokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid or expired verification token");
        }

        VerificationToken verificationToken = optionalToken.get();

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token has expired");
        }

        CustomerUser user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        tokenRepository.delete(verificationToken); // Token used — delete it

        return ResponseEntity.ok("Email successfully verified. You can now login.");
    }

    // Step 3: Login only after email is verified
    public ResponseEntity<JwtResponse> login(LoginRequest request) {
        CustomerUser user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().build();
        }

        if (!user.isEmailVerified()) {
            return ResponseEntity.badRequest().build(); // email not verified
        }

        String token = jwtUtil.generateToken(user.getEmail(), "ROLE_USER");
        return ResponseEntity.ok(new JwtResponse(token));
    }
    
    
    
    
    
    public ResponseEntity<String> forgotPassword(ForgotPasswordRequest request) {
        CustomerUser user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("Email not found");
        }

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();
        tokenRepository.save(verificationToken);

        emailService.sendPasswordResetEmail(user.getEmail(), token);

        return ResponseEntity.ok("Reset link sent to email.");
    }

    public ResponseEntity<String> resetPassword(ResetPasswordRequest request) {
        VerificationToken verificationToken = tokenRepository.findByToken(request.getToken()).orElse(null);
        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }

        CustomerUser user = verificationToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        tokenRepository.delete(verificationToken); // one-time use

        return ResponseEntity.ok("Password reset successfully.");
    }

    
    
}
