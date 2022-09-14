package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "jwt_tokens")
public class TokenJWT {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String hash;
}
