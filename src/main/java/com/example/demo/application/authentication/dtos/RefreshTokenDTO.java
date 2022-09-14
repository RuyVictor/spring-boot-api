package com.example.demo.application.authentication.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class RefreshTokenDTO {
    @Data
    public static class Input {
        @NotBlank
        private String accessToken;
        @NotBlank
        private String refreshToken;
    }

    @Data
    @AllArgsConstructor
    public static class Output {
        private String newAccessToken;
    }
}
