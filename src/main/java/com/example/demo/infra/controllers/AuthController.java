package com.example.demo.infra.controllers;

import com.example.demo.application.authentication.dtos.RefreshTokenDTO;
import com.example.demo.application.authentication.dtos.RevokeTokensDTO;
import com.example.demo.application.authentication.dtos.SignInDTO;
import com.example.demo.application.authentication.dtos.SignUpDTO;
import com.example.demo.application.authentication.usecases.RefreshTokenUseCase;
import com.example.demo.application.authentication.usecases.RevokeTokensUseCase;
import com.example.demo.application.authentication.usecases.SignInUseCase;
import com.example.demo.application.authentication.usecases.SignUpUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final SignInUseCase signInUseCase;
    private final SignUpUseCase signUpUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final RevokeTokensUseCase revokeTokensUseCase;

    @PostMapping("/sign-in")
    public ResponseEntity<SignInDTO.Output> signIn (@RequestBody @Valid SignInDTO.Input input) {
        SignInDTO.Output result = signInUseCase.execute(input);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpDTO.Output> signUp (@RequestBody @Valid SignUpDTO.Input input) {
        SignUpDTO.Output result = signUpUseCase.execute(input);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenDTO.Output> refreshToken (@RequestBody @Valid RefreshTokenDTO.Input input) {
        RefreshTokenDTO.Output result = refreshTokenUseCase.execute(input);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/revoke-tokens")
    public ResponseEntity<RevokeTokensDTO.Output> revokeTokens (@RequestBody @Valid RevokeTokensDTO.Input input) {
        RevokeTokensDTO.Output result = revokeTokensUseCase.execute(input);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test () {
        String result = "Accessible route!";
        return ResponseEntity.ok().body(result);
    }
}
