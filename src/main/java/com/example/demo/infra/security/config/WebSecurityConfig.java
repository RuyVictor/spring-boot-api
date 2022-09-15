package com.example.demo.infra.security.config;

import com.example.demo.application.user.repositories.UserRepository;
import com.example.demo.infra.security.services.AppUserService;
import com.example.demo.infra.security.filters.JwtFilter;
import com.example.demo.infra.security.services.CustomAuthenticationEntryPoint;
import com.example.demo.infra.security.services.TokenJWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserRepository userRepository;
    private final TokenJWTService tokenJWTService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Bean
    public UserDetailsService userDetailsService() {
        return new AppUserService();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v*/auth/sign-in").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v*/auth/sign-up").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v*/auth/refresh-token").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v*/auth/revoke-tokens").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v*/auth/test").authenticated()
                .anyRequest().authenticated();

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(
                new JwtFilter(userRepository, tokenJWTService, customAuthenticationEntryPoint),
                UsernamePasswordAuthenticationFilter.class
        );
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
