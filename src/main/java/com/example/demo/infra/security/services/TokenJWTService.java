package com.example.demo.infra.security.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.application.authentication.dtos.TokenDTO;
import com.example.demo.application.authentication.errors.InvalidAccessTokenException;
import com.example.demo.application.authentication.repositories.AuthRepository;
import com.example.demo.domain.TokenJWT;
import com.example.demo.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenJWTService {

    private final AuthRepository authRepository;
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

    public String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.split(" ")[1].trim();
            return token;
        }
        return null;
    }

    public void isTokenRevoked(String hash) {
        Optional<TokenJWT> tokenInDB = authRepository.findByHash(hash);
        if (tokenInDB.isPresent()) {
            throw new InvalidAccessTokenException();
        }
    }
}
