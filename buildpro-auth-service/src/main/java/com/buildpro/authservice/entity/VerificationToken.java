package com.buildpro.authservice.entity;

import java.time.LocalDateTime;

import com.buildpro.authservice.entity.TokenType; // ✅ Correct import

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    private LocalDateTime expiryDate;

    @Enumerated(EnumType.STRING) // ✅ Add this to store enum as String
    private TokenType tokenType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private CustomerUser user;
}
