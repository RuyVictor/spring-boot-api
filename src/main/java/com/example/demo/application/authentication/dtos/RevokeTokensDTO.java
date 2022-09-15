package com.example.demo.application.authentication.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RevokeTokensDTO {
    @Data
    public static class Input {
        @NotBlank
        @Size(max = 200)
        private String accessToken;
        @NotBlank
        @Size(max = 200)
        private String refreshToken;
    }

    @Data
    @AllArgsConstructor
    public static class Output {
        private String message;
    }
}
