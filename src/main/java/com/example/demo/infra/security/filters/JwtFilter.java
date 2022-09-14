package com.example.demo.infra.security.filters;

import com.example.demo.application.authentication.errors.UserNotFoundException;
import com.example.demo.infra.security.services.TokenJWTService;
import com.example.demo.application.user.repositories.UserRepository;
import com.example.demo.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.split(" ")[1].trim();
            this.authenticate(token);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(String token) {
        String id = tokenJWTService.getUserIdInToken(token);

        User userExists = userRepository.findById(UUID.fromString(id)).orElseThrow(UserNotFoundException::new);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userExists, null, userExists.getRoles());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
