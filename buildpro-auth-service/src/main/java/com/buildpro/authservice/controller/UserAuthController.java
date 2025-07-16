package com.buildpro.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buildpro.authservice.dto.ForgotPasswordRequest;
import com.buildpro.authservice.dto.JwtResponse;
import com.buildpro.authservice.dto.LoginRequest;
import com.buildpro.authservice.dto.RegisterRequest;
import com.buildpro.authservice.dto.ResetPasswordRequest;
import com.buildpro.authservice.service.UserAuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return userAuthService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        return userAuthService.login(request);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return userAuthService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        return userAuthService.resetPassword(request);
    }
    
    @GetMapping
    public ResponseEntity<String> getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // This is the email/username from the JWT
        return ResponseEntity.ok("Welcome, " + username + "! This is your profile.");
    }
}
