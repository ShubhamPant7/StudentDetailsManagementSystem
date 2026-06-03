package com.shubhampant.studentDetailsSystem.config;

import com.shubhampant.studentDetailsSystem.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig (CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                         // Swagger endpoints
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Export and upload restricted to ADMIN
                        .requestMatchers(
                                "/students/export",
                                "/students/upload"
                        ).hasRole("ADMIN")

                        // Create student
                        .requestMatchers(
                                HttpMethod.POST,
                                "/students"
                        ).hasRole("ADMIN")

                        // Update student
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/students/**"
                        ).hasRole("ADMIN")

                        // Delete student
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/students/**"
                        ).hasRole("ADMIN")

                        // Read operation
                        .requestMatchers(
                                HttpMethod.GET,
                                "/students/**"
                        ).hasAnyRole("USER", "ADMIN")

                        // Everything else requires authentication
                        .anyRequest()
                        .authenticated()
                )

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}