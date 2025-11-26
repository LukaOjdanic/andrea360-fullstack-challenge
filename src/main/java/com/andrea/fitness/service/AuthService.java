package com.andrea.fitness.service;

import com.andrea.fitness.model.User;

public interface AuthService {
    User registerEmployee(String firstName, String lastName, String email, String password, String locationId);
    User registerMember(String firstName, String lastName, String email, String password, String locationId);
    String authenticate(String email, String password);
    // AuthResponse login(LoginRequest request);
    User getCurrentUser(); // from security context

}
