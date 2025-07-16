package com.buildpro.authservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String adminName;

    @Column(unique = true, nullable = false)
    private String email;
    private String role = "ROLE_ADMIN";

    private String password;
}
