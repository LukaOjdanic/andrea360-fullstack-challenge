package com.andrea.fitness.dto.entityDto;

import java.math.BigDecimal;
import java.util.UUID;

public record TrainingServiceDto(
    UUID id,
    String name,
    BigDecimal price
) {
    
}
