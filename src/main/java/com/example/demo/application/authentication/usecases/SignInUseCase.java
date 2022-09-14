package com.example.demo.application.authentication.usecases;

import com.example.demo.application.authentication.dtos.SignInDTO;
import com.example.demo.application.authentication.dtos.TokenDTO;
import com.example.demo.infra.security.services.TokenJWTService;
import com.example.demo.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SignInUseCase {
    private final AuthenticationManager authenticationManager;
    private final TokenJWTService tokenJWTService;

    public SignInDTO.Output execute(SignInDTO.Input input) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        User userInDB = (User) authentication.getPrincipal();

        TokenDTO token = tokenJWTService.generateTokens(authentication);
        String accessToken = token.getAccessToken();
        String refreshToken = token.getRefreshToken();

        return new SignInDTO.Output(userInDB, accessToken, refreshToken);
    }
}
