package com.andrea.fitness.dto.realTimeDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatUpdateDto {
    private UUID appointmentId;
    private long remainingSeats;
}
