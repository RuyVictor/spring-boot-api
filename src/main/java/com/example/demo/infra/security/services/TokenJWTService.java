package com.example.demo.infra.security.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.application.authentication.dtos.TokenDTO;
import com.example.demo.application.authentication.errors.InvalidAccessTokenException;
import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenJWTService {
    @Value("${jwt.accessTokenExpiration}")
    private Integer accessTokenExpirationMinutes;
    @Value("${jwt.refreshTokenExpiration}")
    private Integer refreshTokenExpirationMinutes;
    @Value("${jwt.secret}")
    private String secret;

    public TokenDTO generateTokens(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        String accessToken = JWT.create().withSubject(user.getId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationMinutes * 60 * 1000))
                .sign(algorithm);
        String refreshToken = JWT.create().withSubject(user.getId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpirationMinutes * 60 * 1000))
                .sign(algorithm);

        return new TokenDTO(accessToken, refreshToken);
    }
    public TokenDTO generateTokensWithoutAuth(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        String accessToken = JWT.create().withSubject(user.getId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationMinutes * 60 * 1000))
                .sign(algorithm);
        String refreshToken = JWT.create().withSubject(user.getId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpirationMinutes * 60 * 1000))
                .sign(algorithm);

        return new TokenDTO(accessToken, refreshToken);
    }

    public String getUserIdInToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();
        } catch (Exception e) {
            throw new InvalidAccessTokenException();
        }
    }
}
