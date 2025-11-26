package com.andrea.fitness.controller;

import com.andrea.fitness.dto.security.LoginRequest;
import com.andrea.fitness.model.Location;
import com.andrea.fitness.model.Role;
import com.andrea.fitness.model.User;
import com.andrea.fitness.repository.LocationRepository;
import com.andrea.fitness.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        locationRepository.deleteAll();
    }

    @Test
    void login_Success() throws Exception {

        // Create a dumny user
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(Role.MEMBER);

        Location location = new Location();
        location.setAddress("123 Test St");
        location.setName("Test Gym");
        location = locationRepository.save(location);

        user.setLocation(location);
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.role").value("MEMBER"));
    }

}
