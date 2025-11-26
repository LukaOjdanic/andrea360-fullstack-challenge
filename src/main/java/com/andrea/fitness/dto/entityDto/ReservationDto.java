package com.andrea.fitness.dto.entityDto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationDto(
    UUID id,
    UUID memberId,
    String memberName,
    UUID appointmentId,
    LocalDateTime reservedAt,
    String serviceName,
    LocalDateTime appointmentTime
) {
    
}
