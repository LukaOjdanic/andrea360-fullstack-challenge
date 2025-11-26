package com.andrea.fitness.dto.createRequestDto;

import jakarta.validation.constraints.NotBlank;

public record LocationCreateRequestDto(
    @NotBlank String name,
    @NotBlank String address
) {
    
}
