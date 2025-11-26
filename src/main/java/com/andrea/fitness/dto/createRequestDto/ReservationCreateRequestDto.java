package com.andrea.fitness.dto.createRequestDto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record ReservationCreateRequestDto(
    @NotNull UUID appointmentId,
    @NotNull UUID purchaseId
) {
    
}
