package com.example.demo.application.authentication.usecases;

import com.example.demo.application.authentication.dtos.RevokeTokensDTO;
import com.example.demo.application.authentication.repositories.AuthRepository;
import com.example.demo.domain.TokenJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RevokeTokensUseCase {
    private final AuthRepository authRepository;

    public RevokeTokensDTO.Output execute(RevokeTokensDTO.Input input) {
        TokenJWT accessToken = new TokenJWT();
        TokenJWT refreshToken = new TokenJWT();
        accessToken.setHash(input.getAccessToken());
        refreshToken.setHash(input.getRefreshToken());
        authRepository.save(accessToken);
        authRepository.save(refreshToken);

        return new RevokeTokensDTO.Output("Tokens revoked!");
    }
}
