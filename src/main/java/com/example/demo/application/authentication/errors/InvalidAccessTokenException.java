package com.example.demo.application.authentication.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidAccessTokenException extends RuntimeException {
    public InvalidAccessTokenException() {
        super("Invalid access token!");
    }
}
