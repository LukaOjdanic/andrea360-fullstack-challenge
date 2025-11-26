package com.andrea.fitness.dto.entityDto;

import java.util.UUID;

public record UserDto(
    UUID id,
    String firstName,
    String lastName,
    String email,
    String role,
    UUID locationId
) {
    
}
