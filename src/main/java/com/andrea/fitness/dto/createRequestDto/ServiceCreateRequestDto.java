package com.andrea.fitness.dto.createRequestDto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record ServiceCreateRequestDto(    
    @NotBlank String name,
    @DecimalMin(value = "0.01") BigDecimal price
) {
    
}
