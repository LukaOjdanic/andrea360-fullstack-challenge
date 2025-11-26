package com.andrea.fitness.dto.createRequestDto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PaymentIntentCreateRequestDto(
    @NotNull UUID serviceId
) {}
