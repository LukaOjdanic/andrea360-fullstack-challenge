package com.andrea.fitness.service;

import com.andrea.fitness.model.Appointment;
import com.andrea.fitness.model.Location;
import com.andrea.fitness.model.Role;
import com.andrea.fitness.model.TrainingService;
import com.andrea.fitness.model.User;
import com.andrea.fitness.repository.AppointmentRepository;
import com.andrea.fitness.repository.LocationRepository;
import com.andrea.fitness.repository.ReservationRepository;
import com.andrea.fitness.repository.TrainingServiceRepository;
import com.andrea.fitness.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrainingServiceRepository serviceRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private UUID locationId;
    private UUID serviceId;
    private Location location;
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        locationId = UUID.randomUUID();
        serviceId = UUID.randomUUID();

        location = new Location();
        location.setId(locationId);

        trainingService = new TrainingService();
        trainingService.setId(serviceId);
    }

    @Test
    void createMember_Success() {
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = employeeService.createMember("First", "Last", email, "pass", locationId.toString());

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(Role.MEMBER, result.getRole());
        assertEquals(location, result.getLocation());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createMember_EmailExists() {
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> 
            employeeService.createMember("First", "Last", email, "pass", locationId.toString())
        );
    }

    @Test
    void createService_Success() {
        when(serviceRepository.save(any(TrainingService.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TrainingService result = employeeService.createService("Yoga", 50.0);

        assertNotNull(result);
        assertEquals("Yoga", result.getName());
        assertEquals(BigDecimal.valueOf(50.0), result.getPrice());
        verify(serviceRepository).save(any(TrainingService.class));
    }

    @Test
    void createAppointment_Success() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);
        
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(trainingService));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Appointment result = employeeService.createAppointment(start, end, 10, locationId, serviceId);

        assertNotNull(result);
        assertEquals(start, result.getStartTime());
        assertEquals(end, result.getEndTime());
        assertEquals(10, result.getMaxCapacity());
        assertEquals(location, result.getLocation());
        assertEquals(trainingService, result.getService());
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void createAppointment_InvalidTime() {
        LocalDateTime start = LocalDateTime.now().plusHours(2);
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        assertThrows(IllegalArgumentException.class, () -> 
            employeeService.createAppointment(start, end, 10, locationId, serviceId)
        );
    }
}
