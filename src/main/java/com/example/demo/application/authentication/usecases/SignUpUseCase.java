package com.example.demo.application.authentication.usecases;

import com.example.demo.application.authentication.dtos.SignUpDTO;
import com.example.demo.application.authentication.dtos.TokenDTO;
import com.example.demo.application.authentication.errors.UserAlreadyExistsException;
import com.example.demo.application.user.repositories.UserRepository;
import com.example.demo.domain.User;
import com.example.demo.infra.security.services.TokenJWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SignUpUseCase {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepo;
    private final TokenJWTService tokenJWTService;
    private final BCryptPasswordEncoder passwordEncoder;

    public SignUpDTO.Output execute(SignUpDTO.Input input) {
        userRepo.findByEmail(input.getEmail()).ifPresent(s -> { throw new UserAlreadyExistsException(); });

        String encodedPassword = passwordEncoder.encode(input.getPassword());

        User user = new User();
        user.setName(input.getName());
        user.setUsername(input.getUsername());
        user.setEmail(input.getEmail());
        user.setPassword(encodedPassword);

        User userInDB = userRepo.save(user);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userInDB.getEmail(), input.getPassword());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        TokenDTO token = tokenJWTService.generateTokens(authentication);
        String accessToken = token.getAccessToken();
        String refreshToken = token.getRefreshToken();

        return new SignUpDTO.Output(user, accessToken, refreshToken);
    }
}
