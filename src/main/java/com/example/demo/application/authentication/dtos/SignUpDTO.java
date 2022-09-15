package com.example.demo.application.authentication.dtos;

import com.example.demo.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignUpDTO  {
    @Data
    public static class Input {
        @NotBlank
        @Size(max=50)
        private String name;
        @NotBlank
        @Size(max=15)
        private String username;
        @NotBlank
        @Email
        @Size(max=40)
        private String email;
        @NotBlank
        @Size(max=15)
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
