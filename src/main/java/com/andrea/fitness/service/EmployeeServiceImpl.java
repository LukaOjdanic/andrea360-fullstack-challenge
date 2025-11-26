package com.andrea.fitness.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.andrea.fitness.model.Appointment;
import com.andrea.fitness.model.Location;
import com.andrea.fitness.model.Reservation;
import com.andrea.fitness.model.Role;
import com.andrea.fitness.model.TrainingService;
import com.andrea.fitness.model.User;
import com.andrea.fitness.repository.AppointmentRepository;
import com.andrea.fitness.repository.LocationRepository;
import com.andrea.fitness.repository.ReservationRepository;
import com.andrea.fitness.repository.TrainingServiceRepository;
import com.andrea.fitness.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{
    private final UserRepository userRepository;
    private final TrainingServiceRepository serviceRepository;
    private final AppointmentRepository appointmentRepository;
    private final ReservationRepository reservationRepository;
    private final LocationRepository locationRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User createMember(String firstName, String lastName, String email, String password, String locationId) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Member email already exists");
        }
        Location location = locationRepository.findById(UUID.fromString(locationId))
            .orElseThrow(() -> new IllegalArgumentException("Invalid location"));
        User member = new User();
        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setEmail(email);
        member.setPassword(passwordEncoder.encode(password));
        member.setRole(Role.valueOf("MEMBER"));
        member.setLocation(location);
        return userRepository.save(member);
    }

    @Override
    @Transactional
    public TrainingService createService(String name, double price) {
        TrainingService service = new TrainingService();
        service.setName(name);
        service.setPrice(java.math.BigDecimal.valueOf(price));
        return serviceRepository.save(service);
    }

    @Override
    public List<TrainingService> getAllServices() {
        return serviceRepository.findAll();
    }

    @Override
    @Transactional
    public Appointment createAppointment(LocalDateTime startTime, LocalDateTime endTime,
                                         int maxCapacity, UUID locationId, UUID serviceId) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        Location location = locationRepository.findById(locationId)
            .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        TrainingService service = serviceRepository.findById(serviceId)
            .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        Appointment appointment = new Appointment();
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setMaxCapacity(maxCapacity);
        appointment.setLocation(location);
        appointment.setService(service);
        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> getAppointmentsByLocation(UUID locationId) {
        return appointmentRepository.findByLocationId(locationId);
    }

    @Override
    public List<Appointment> getFutureAppointments() {
        return appointmentRepository.findFutureAppointments(LocalDateTime.now());
    }

    @Override
    public int getAvailableSpots(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        return appointment.getAvailableSpots();
    }

    @Override
    public List<User> getMembersInAppointment(UUID appointmentId) {
        return reservationRepository.findByAppointmentId(appointmentId).stream()
            .map(Reservation::getMember)
            .collect(Collectors.toList());
    }

}
