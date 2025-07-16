package com.buildpro.authservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String role = "ROLE_USER";

    private String password;
    @Column(nullable = false)
    private boolean isEmailVerified;
}
