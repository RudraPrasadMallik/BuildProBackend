package com.buildpro.authservice.controller;

import com.buildpro.authservice.dto.JwtResponse;
import com.buildpro.authservice.dto.LoginRequest;
import com.buildpro.authservice.dto.RegisterRequest;
import com.buildpro.authservice.service.AdminAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return adminAuthService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        return adminAuthService.login(request);
    }
}
