package com.shubhampant.studentDetailsSystem.Service;

import com.shubhampant.studentDetailsSystem.entity.UserEntity;
import com.shubhampant.studentDetailsSystem.repository.UserRepository;
import com.shubhampant.studentDetailsSystem.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTests {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    void testLoadUserByUsernameSuccess() {

        UserEntity user = new UserEntity();

        user.setUsername("admin");
        user.setPassword("password");
        user.setRole("ADMIN");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername("admin");

        assertEquals("admin", result.getUsername());
        assertEquals("password", result.getPassword());

        verify(userRepository).findByUsername("admin");
    }

    @Test
    void testLoadUserByUsernameNotFound() {

        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("admin"));

        verify(userRepository).findByUsername("admin");
    }
}
