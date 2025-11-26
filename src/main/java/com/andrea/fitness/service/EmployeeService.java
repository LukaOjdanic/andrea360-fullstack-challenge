package com.andrea.fitness.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.andrea.fitness.model.Appointment;
import com.andrea.fitness.model.TrainingService;
import com.andrea.fitness.model.User;

public interface EmployeeService {
    User createMember(String firstName, String lastName, String email, String password, String locationId);

    // Training service
    TrainingService createService(String name, double price);
    List<TrainingService> getAllServices();

    // Appointment 
    Appointment createAppointment(
        LocalDateTime startTime,
        LocalDateTime endTime,
        int maxCapacity,
        UUID locationId,
        UUID serviceId
    );
    List<Appointment> getAppointmentsByLocation(UUID locationId);
    List<Appointment> getFutureAppointments();

    // Reservation
    int getAvailableSpots(UUID appointmentId);
    List<User> getMembersInAppointment(UUID appointmentId);

}
