package com.andrea.fitness.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andrea.fitness.model.Location;
import com.andrea.fitness.model.Role;
import com.andrea.fitness.model.User;
import com.andrea.fitness.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerEmployee(String firstName, String lastName, String email, String password, String locationId) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.getRole();
        user.setRole(Role.valueOf("EMPLOYEE"));
        user.setLocation(new Location()); // resolved below
        user.getLocation().setId(UUID.fromString(locationId));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User registerMember(String firstName, String lastName, String email, String password, String locationId) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.valueOf("MEMBER"));
        user.setLocation(new Location());
        user.getLocation().setId(UUID.fromString(locationId));
        return userRepository.save(user);
    }

    @Override
    public String authenticate(String email, String password) {
        throw new UnsupportedOperationException("Use JWT flow with /login endpoint");
    }

    @Override
    public User getCurrentUser() {
        // Should extract from SecurityContext
        throw new UnsupportedOperationException("Implement via @AuthenticationPrincipal User");
    }
}