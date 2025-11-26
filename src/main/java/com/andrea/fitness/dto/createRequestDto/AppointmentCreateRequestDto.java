package com.andrea.fitness.dto.createRequestDto;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AppointmentCreateRequestDto(
    @Future @NotNull LocalDateTime startTime,
    @Future @NotNull LocalDateTime endTime,
    @Min(1) int maxCapacity,
    @NotNull UUID locationId,
    @NotNull UUID serviceId
) {
    
}
