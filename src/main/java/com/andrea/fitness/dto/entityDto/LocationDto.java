package com.andrea.fitness.dto.entityDto;

import java.util.UUID;

public record LocationDto(
    UUID id,
    String name,
    String address
) {
    
}
