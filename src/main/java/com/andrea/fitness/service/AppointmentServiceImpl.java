package com.andrea.fitness.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.andrea.fitness.model.Appointment;
import com.andrea.fitness.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService{
    private final AppointmentRepository appointmentRepository;

    @Override
    public Appointment getAppointmentWithDetails(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
    }

    @Override
    public int getAvailableSpots(UUID appointmentId) {
        return getAppointmentWithDetails(appointmentId).getAvailableSpots();
    }

    @Override
    public boolean isAppointmentFull(UUID appointmentId) {
        return getAvailableSpots(appointmentId) <= 0;
    }
}
