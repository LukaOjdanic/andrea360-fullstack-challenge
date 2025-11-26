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
import org.springframework.web.bind.annotation.RestController;

import com.andrea.fitness.dto.createRequestDto.EmployeeCreateRequestDto;
import com.andrea.fitness.dto.createRequestDto.LocationCreateRequestDto;
import com.andrea.fitness.dto.entityDto.LocationDto;
import com.andrea.fitness.dto.entityDto.UserDto;
import com.andrea.fitness.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Admin management APIs")
public class AdminController {
        private final AdminService adminService;

    @PostMapping("/locations")
    @Operation(summary = "Create a new location")
    public ResponseEntity<LocationDto> createLocation(@Valid @RequestBody LocationCreateRequestDto request) {
        var location = adminService.createLocation(request.name(), request.address());
        System.out.println("location is being created");
        return ResponseEntity.ok(new LocationDto(location.getId(), location.getName(), location.getAddress()));
    }

    @GetMapping("/locations")
    @Operation(summary = "Get all locations")
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        return ResponseEntity.ok(
            adminService.getAllLocations().stream()
                .map(l -> new LocationDto(l.getId(), l.getName(), l.getAddress()))
                .toList()
        );
    }

    @PostMapping("/employees")
    @Operation(summary = "Create a new employee")
    public ResponseEntity<UserDto> createEmployee(@Valid @RequestBody EmployeeCreateRequestDto request) {
        var employee = adminService.createEmployee(
            request.firstName(),
            request.lastName(),
            request.email(),
            request.password(),
            request.locationId()
        );
        return ResponseEntity.ok(new UserDto(
            employee.getId(),
            employee.getFirstName(),
            employee.getLastName(),
            employee.getEmail(),
            employee.getRole().name(),
            employee.getLocation().getId()
        ));
    }

    @GetMapping("/locations/{locationId}/employees")
    @Operation(summary = "Get all employees for a location")
    public ResponseEntity<List<UserDto>> getEmployees(@PathVariable UUID locationId) {
        return ResponseEntity.ok(
            adminService.getEmployeesByLocation(locationId).stream()
                .map(e -> new UserDto(
                    e.getId(),
                    e.getFirstName(),
                    e.getLastName(),
                    e.getEmail(),
                    e.getRole().name(),
                    e.getLocation().getId()
                ))
                .toList()
        );
    }

}
