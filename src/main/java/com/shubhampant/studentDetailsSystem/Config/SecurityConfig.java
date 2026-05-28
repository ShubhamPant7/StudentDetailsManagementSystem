package com.shubhampant.studentDetailsSystem.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {

        UserDetails admin =
                User.withDefaultPasswordEncoder()
                        .username("admin")
                        .password("admin123")
                        .roles("ADMIN")
                        .build();

        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("user123")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(
                admin,
                user
        );
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

                        // Read operations
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