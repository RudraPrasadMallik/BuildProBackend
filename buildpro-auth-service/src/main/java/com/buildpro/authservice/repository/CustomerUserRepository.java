package com.buildpro.authservice.repository;

import com.buildpro.authservice.entity.CustomerUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerUserRepository extends JpaRepository<CustomerUser, Long> {
    Optional<CustomerUser> findByEmail(String email);
}
