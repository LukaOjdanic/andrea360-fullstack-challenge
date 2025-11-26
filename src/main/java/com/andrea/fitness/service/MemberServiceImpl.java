package com.andrea.fitness.service;

import com.andrea.fitness.dto.realTimeDto.SeatUpdateDto;
import com.andrea.fitness.model.Appointment;
import com.andrea.fitness.model.Purchase;
import com.andrea.fitness.model.Reservation;
import com.andrea.fitness.repository.AppointmentRepository;
import com.andrea.fitness.repository.PurchaseRepository;
import com.andrea.fitness.repository.ReservationRepository;
import com.andrea.fitness.websocket.ReservationUpdateController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final ReservationRepository reservationRepository;
    private final AppointmentRepository appointmentRepository;
    private final PurchaseRepository purchaseRepository;
    private final ReservationUpdateController reservationUpdateController;

    @Override
    public Purchase initiatePurchase(UUID serviceId, String stripeToken) {
        // Placeholder implementation
        return null;
    }

    @Override
    public Purchase confirmPurchase(String paymentIntentId) {
        // Placeholder implementation
        return null;
    }

    @Override
    public List<Purchase> getAvailablePurchases() {
        // Placeholder implementation
        return Collections.emptyList();
    }

    @Override
    public Reservation reserveAppointment(UUID appointmentId, UUID purchaseId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment not found"));
        Purchase purchase = purchaseRepository.findById(purchaseId).orElseThrow(() -> new RuntimeException("Purchase not found"));

        if (purchase.getUsesLeft() <= 0) {
            throw new RuntimeException("No uses left on this purchase");
        }

        long currentReservations = reservationRepository.countByAppointment(appointment);
        if (currentReservations >= appointment.getMaxCapacity()) {
            throw new RuntimeException("No seats available");
        }

        Reservation reservation = new Reservation();
        reservation.setAppointment(appointment);
        reservation.setPurchase(purchase);

        purchase.setUsesLeft(purchase.getUsesLeft() - 1);

        reservationRepository.save(reservation);
        purchaseRepository.save(purchase);

        long remainingSeats = appointment.getMaxCapacity() - (currentReservations + 1);
        reservationUpdateController.sendUpdate(new SeatUpdateDto(appointmentId, remainingSeats));

        return reservation;
    }

    @Override
    public List<Reservation> getMyReservations() {
        // Placeholder implementation
        return Collections.emptyList();
    }

    @Override
    public boolean canReserve(UUID appointmentId) {
        // Placeholder implementation - assuming true for now
        return true;
    }
}
