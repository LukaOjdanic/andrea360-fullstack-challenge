package com.andrea.fitness.service;

import java.util.List;
import java.util.UUID;

import com.andrea.fitness.model.Purchase;
import com.andrea.fitness.model.Reservation;

public interface MemberService {
    // Purchase flow
    Purchase initiatePurchase(UUID serviceId, String stripeToken); // or PaymentMethod ID (check later)
    Purchase confirmPurchase(String paymentIntentId); // called by webhook or client
    List<Purchase> getAvailablePurchases(); // succeeded purchases

    // Reservation
    Reservation reserveAppointment(UUID appointmentId, UUID purchaseId);
    List<Reservation> getMyReservations();

    // Validation helpers
    boolean canReserve(UUID appointmentId);

}
