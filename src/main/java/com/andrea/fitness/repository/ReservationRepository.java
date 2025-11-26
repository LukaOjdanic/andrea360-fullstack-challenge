package com.andrea.fitness.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.andrea.fitness.model.Appointment;
import com.andrea.fitness.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    boolean existsByMemberIdAndAppointmentId(UUID memberId, UUID appointmentId);

    // to count used spots
    List<Reservation> findByAppointmentId(UUID appointmentId);

    long countByAppointment(Appointment appointment);

    // Get all reservations by member
    List<Reservation> findByMemberId(UUID memberId);

    // Get reservation with purchase details (for reporting)
    @Query("SELECT r FROM Reservation r " +
           "JOIN FETCH r.purchase " +
           "JOIN FETCH r.appointment a " +
           "JOIN FETCH a.service " +
           "WHERE r.id = :id")
    Optional<Reservation> findDetailedById(UUID id);

}
