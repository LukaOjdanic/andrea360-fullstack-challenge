package com.andrea.fitness.controller;

import com.andrea.fitness.dto.entityDto.AppointmentDto;
import com.andrea.fitness.dto.entityDto.LocationDto;
import com.andrea.fitness.repository.LocationRepository;
import com.andrea.fitness.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Tag(name = "Public", description = "Public APIs")
public class PublicController {

    private final AppointmentService appointmentService;
    private final LocationRepository locationRepository;

    @GetMapping("/appointments/{id}/details")
    @Operation(summary = "Get appointment details")
    public ResponseEntity<AppointmentDto> getAppointment(@PathVariable UUID id) {
        var appt = appointmentService.getAppointmentWithDetails(id);
        return ResponseEntity.ok(new AppointmentDto(
            appt.getId(),
            appt.getStartTime(),
            appt.getEndTime(),
            appt.getMaxCapacity(),
            appt.getAvailableSpots(),
            appt.getLocation().getId(),
            appt.getService().getId(),
            appt.getService().getName()
        ));
    }

    @GetMapping("/locations")
    @Operation(summary = "Get all locations")
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        List<LocationDto> locations = locationRepository.findAll().stream()
                .map(location -> new LocationDto(location.getId(), location.getName(), location.getAddress()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(locations);
    }
}