package com.buildpro.authservice.service;

import com.buildpro.authservice.dto.JwtResponse;
import com.buildpro.authservice.dto.LoginRequest;
import com.buildpro.authservice.dto.RegisterRequest;
import com.buildpro.authservice.entity.AdminUser;
import com.buildpro.authservice.repository.AdminUserRepository;
import com.buildpro.authservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminUserRepository adminRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public ResponseEntity<JwtResponse> register(RegisterRequest request) {
        if (adminRepo.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        AdminUser admin = AdminUser.builder()
                .adminName(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        adminRepo.save(admin);

        String token = jwtUtil.generateToken(admin.getEmail(), "ROLE_ADMIN");
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<JwtResponse> login(LoginRequest request) {
        AdminUser admin = adminRepo.findByEmail(request.getEmail()).orElse(null);

        if (admin == null || !passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            return ResponseEntity.badRequest().build();
        }

        String token = jwtUtil.generateToken(admin.getEmail(), "ROLE_ADMIN");
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
