package com.example.demo.application.authentication.repositories;

import com.example.demo.domain.TokenJWT;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<TokenJWT, UUID> {
    Optional<TokenJWT> findByHash(String hash);
}
