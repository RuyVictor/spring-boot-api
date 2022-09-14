package com.example.demo.application.authentication.usecases;

import com.example.demo.application.authentication.dtos.RefreshTokenDTO;
import com.example.demo.application.authentication.dtos.TokenDTO;
import com.example.demo.application.authentication.errors.InvalidRefreshTokenException;
import com.example.demo.application.authentication.errors.UserNotFoundException;
import com.example.demo.application.authentication.repositories.AuthRepository;
import com.example.demo.application.user.repositories.UserRepository;
import com.example.demo.domain.TokenJWT;
import com.example.demo.infra.security.services.TokenJWTService;
import com.example.demo.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenUseCase {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepo;
    private final AuthRepository authRepository;
    private final TokenJWTService tokenJWTService;

    public RefreshTokenDTO.Output execute(RefreshTokenDTO.Input input) {

        String userId = tokenJWTService.getUserIdInToken(input.getRefreshToken());

        User user = userRepo.findById(UUID.fromString(userId)).orElseThrow(UserNotFoundException::new);

        authRepository.findByHash(input.getRefreshToken()).ifPresent(s -> { throw new InvalidRefreshTokenException(); });

        TokenJWT accessToken = new TokenJWT();
        accessToken.setHash(input.getAccessToken());
        authRepository.save(accessToken); // revoking current Access Token!

        TokenDTO token = tokenJWTService.generateTokensWithoutAuth(user);
        String newAccessToken = token.getAccessToken();

        return new RefreshTokenDTO.Output(newAccessToken);
    }
}
