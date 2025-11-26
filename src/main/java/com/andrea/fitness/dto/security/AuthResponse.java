package com.andrea.fitness.dto.security;

import java.util.UUID;

public record AuthResponse(
    String token, // JWT
    String role,
    UUID userId
) {
    
}
