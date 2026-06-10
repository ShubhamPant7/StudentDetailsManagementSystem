package com.shubhampant.studentDetailsSystem.service;


import com.shubhampant.studentDetailsSystem.entity.UserEntity;
import com.shubhampant.studentDetailsSystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//Service layer for UserDetails related operations. Includes password encoding.
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    public CustomUserDetailsService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Loading user details for username={}", username);

        UserEntity user = userRepository.findByUsername(username).orElseThrow(() ->
        {
            log.warn("Authentication attempt for non existing user= {}", username);
            return new UsernameNotFoundException("User not found");
        });

        log.debug("Successfully loaded user details for username={}, role={}", user.getUsername(), user.getRole());

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    //Implements BCryptPasswordEncoder for password encoding.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
