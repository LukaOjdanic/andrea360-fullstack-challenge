package com.andrea.fitness.dto.entityDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.andrea.fitness.model.PurchaseStatus;

public record PurchaseDto(
    UUID id,
    UUID serviceId,
    String serviceName,
    BigDecimal amount,
    PurchaseStatus status,
    LocalDateTime purchasedAt

) {
    
}
