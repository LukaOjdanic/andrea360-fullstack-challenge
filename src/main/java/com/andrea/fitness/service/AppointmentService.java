package com.andrea.fitness.service;

import java.util.UUID;

import com.andrea.fitness.model.Appointment;

public interface AppointmentService {
    Appointment getAppointmentWithDetails(UUID appointmentId);
    int getAvailableSpots(UUID appointmentId);
    boolean isAppointmentFull(UUID appointmentId);

}
