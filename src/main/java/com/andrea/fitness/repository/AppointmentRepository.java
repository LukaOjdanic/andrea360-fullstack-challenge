package com.andrea.fitness.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.andrea.fitness.model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID>{
    List<Appointment> findByLocationId(UUID locationId);

    @Query("SELECT a FROM Appointment a WHERE a.startTime > :now")
    List<Appointment> findFutureAppointments(LocalDateTime now);

    List<Appointment> findByServiceId(UUID serviceId);

}
