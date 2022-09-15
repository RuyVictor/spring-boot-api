package com.example.demo.infra.security.filters;

import com.example.demo.application.authentication.errors.InvalidAccessTokenException;
import com.example.demo.application.authentication.errors.UserNotFoundException;
import com.example.demo.infra.security.services.TokenJWTService;
import com.example.demo.application.user.repositories.UserRepository;
import com.example.demo.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final TokenJWTService tokenJWTService;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = tokenJWTService.getTokenFromHeader(request);
        if (token != null) {
            try{
                tokenJWTService.isTokenRevoked(token);
                String id = tokenJWTService.getUserIdInToken(token);
                User userExists = userRepository.findById(UUID.fromString(id)).orElseThrow(UserNotFoundException::new);
                this.authenticate(userExists);
            } catch (AuthenticationException e) {
                this.authenticationEntryPoint.commence(request, response, e);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void authenticate(User user) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getRoles());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
