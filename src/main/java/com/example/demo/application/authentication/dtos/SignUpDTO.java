package com.example.demo.application.authentication.dtos;

import com.example.demo.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

public class SignUpDTO  {
    @Data
    public static class Input {
        @NotBlank
        private String name;
        @NotBlank
        private String username;
        @NotBlank
        private String email;
        @NotBlank
        private String password;
    }

    @AllArgsConstructor
    @Data
    public static class Output {
        private User user;
        private String accessToken;
        private String refreshToken;
    }
}
