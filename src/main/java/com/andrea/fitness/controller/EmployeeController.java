package com.andrea.fitness.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andrea.fitness.dto.createRequestDto.AppointmentCreateRequestDto;
import com.andrea.fitness.dto.createRequestDto.MemberCreateRequestDto;
import com.andrea.fitness.dto.createRequestDto.ServiceCreateRequestDto;
import com.andrea.fitness.dto.entityDto.AppointmentDto;
import com.andrea.fitness.dto.entityDto.TrainingServiceDto;
import com.andrea.fitness.dto.entityDto.UserDto;
import com.andrea.fitness.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
@Tag(name = "Employee", description = "Employee management APIs")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/members")
    @Operation(summary = "Create a new member")
    public ResponseEntity<UserDto> createMember(@Valid @RequestBody MemberCreateRequestDto request) {
        var member = employeeService.createMember(
            request.firstName(),
            request.lastName(),
            request.email(),
            request.password(),
            request.locationId().toString()
        );
        return ResponseEntity.ok(new UserDto(
            member.getId(),
            member.getFirstName(),
            member.getLastName(),
            member.getEmail(),
            member.getRole().name(),
            member.getLocation().getId()
        ));
    }

    @PostMapping("/services")
    @Operation(summary = "Create a new service")
    public ResponseEntity<TrainingServiceDto> createService(@Valid @RequestBody ServiceCreateRequestDto request) {
        var service = employeeService.createService(request.name(), request.price().doubleValue());
        return ResponseEntity.ok(new TrainingServiceDto(service.getId(), service.getName(), service.getPrice()));
    }

    @GetMapping("/services")
    @Operation(summary = "Get all services")
    public ResponseEntity<List<TrainingServiceDto>> getAllServices() {
        return ResponseEntity.ok(
            employeeService.getAllServices().stream()
                .map(s -> new TrainingServiceDto(s.getId(), s.getName(), s.getPrice()))
                .toList()
        );
    }

    @PostMapping("/appointments")
    @Operation(summary = "Create a new appointment")
    public ResponseEntity<AppointmentDto> createAppointment(@Valid @RequestBody AppointmentCreateRequestDto request) {
        var appt = employeeService.createAppointment(
            request.startTime(),
            request.endTime(),
            request.maxCapacity(),
            request.locationId(),
            request.serviceId()
        );
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

    @GetMapping("/appointments")
    @Operation(summary = "Get all appointments for a location")
    public ResponseEntity<List<AppointmentDto>> getAppointments(@RequestParam UUID locationId) {
        return ResponseEntity.ok(
            employeeService.getAppointmentsByLocation(locationId).stream()
                .map(a -> new AppointmentDto(
                    a.getId(),
                    a.getStartTime(),
                    a.getEndTime(),
                    a.getMaxCapacity(),
                    a.getAvailableSpots(),
                    a.getLocation().getId(),
                    a.getService().getId(),
                    a.getService().getName()
                ))
                .toList()
        );
    }

    @GetMapping("/appointments/future")
    @Operation(summary = "Get all future appointments")
    public ResponseEntity<List<AppointmentDto>> getFutureAppointments() {
        return ResponseEntity.ok(
            employeeService.getFutureAppointments().stream()
                .map(a -> new AppointmentDto(
                    a.getId(),
                    a.getStartTime(),
                    a.getEndTime(),
                    a.getMaxCapacity(),
                    a.getAvailableSpots(),
                    a.getLocation().getId(),
                    a.getService().getId(),
                    a.getService().getName()
                ))
                .toList()
        );
    }

    @GetMapping("/appointments/{appointmentId}/available-spots")
    @Operation(summary = "Get available spots for an appointment")
    public ResponseEntity<Integer> getAvailableSpots(@PathVariable UUID appointmentId) {
        int spots = employeeService.getAvailableSpots(appointmentId);
        return ResponseEntity.ok(spots);
    }
}