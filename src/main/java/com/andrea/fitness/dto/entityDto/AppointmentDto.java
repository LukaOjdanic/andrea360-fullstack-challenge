package com.andrea.fitness.dto.entityDto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentDto(
    UUID id,
    LocalDateTime startTime,
    LocalDateTime endTime,
    int maxCapacity,
    int availableSpots,
    UUID locationId,
    UUID serviceId,
    String serviceName
) {
    
}
