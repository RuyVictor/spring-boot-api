package com.example.demo.application.authentication.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class TokenDTO {
    @NotBlank
    private String accessToken;
    private String refreshToken;
}
