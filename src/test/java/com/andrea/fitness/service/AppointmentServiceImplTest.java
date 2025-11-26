package com.andrea.fitness.service;

import com.andrea.fitness.model.Appointment;
import com.andrea.fitness.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private UUID appointmentId;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        appointmentId = UUID.randomUUID();
        appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setMaxCapacity(10);
    }

    @Test
    void getAppointmentWithDetails_Success() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        Appointment result = appointmentService.getAppointmentWithDetails(appointmentId);

        assertNotNull(result);
        assertEquals(appointmentId, result.getId());
    }

    @Test
    void getAppointmentWithDetails_NotFound() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> appointmentService.getAppointmentWithDetails(appointmentId));
    }

    @Test
    void getAvailableSpots_Success() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        int spots = appointmentService.getAvailableSpots(appointmentId);

        assertEquals(10, spots);
    }
}
