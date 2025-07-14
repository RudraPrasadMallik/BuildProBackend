package com.buildpro.orderservice.config;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final String jwtSecret = "buildpro-secret-key"; // same key used in auth-service

    public String extractUserId(String token) {
        return getClaims(token).getSubject(); // We use username/userId as subject
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }
}
