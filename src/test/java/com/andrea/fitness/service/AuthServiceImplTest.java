package com.andrea.fitness.service;

import com.andrea.fitness.model.Location;
import com.andrea.fitness.model.Role;
import com.andrea.fitness.model.User;
import com.andrea.fitness.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private String firstName = "John";
    private String lastName = "Doe";
    private String email = "john.doe@example.com";
    private String password = "password";
    private String locationId;

    @BeforeEach
    void setUp() {
        locationId = UUID.randomUUID().toString();
    }

    @Test
    void registerMember_Success() {
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(UUID.randomUUID());
            return user;
        });

        User result = authService.registerMember(firstName, lastName, email, password, locationId);

        assertNotNull(result);
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertEquals(email, result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(Role.MEMBER, result.getRole());
        assertEquals(UUID.fromString(locationId), result.getLocation().getId());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerMember_EmailAlreadyExists() {
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.registerMember(firstName, lastName, email, password, locationId));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerEmployee_Success() {
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(UUID.randomUUID());
            return user;
        });

        User result = authService.registerEmployee(firstName, lastName, email, password, locationId);

        assertNotNull(result);
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertEquals(email, result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(Role.EMPLOYEE, result.getRole());
        assertEquals(UUID.fromString(locationId), result.getLocation().getId());

        verify(userRepository).save(any(User.class));
    }
}
