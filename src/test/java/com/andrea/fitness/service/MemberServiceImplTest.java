package com.andrea.fitness.service;

import com.andrea.fitness.dto.realTimeDto.SeatUpdateDto;
import com.andrea.fitness.model.Appointment;
import com.andrea.fitness.model.Purchase;
import com.andrea.fitness.model.Reservation;
import com.andrea.fitness.repository.AppointmentRepository;
import com.andrea.fitness.repository.PurchaseRepository;
import com.andrea.fitness.repository.ReservationRepository;
import com.andrea.fitness.websocket.ReservationUpdateController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private ReservationUpdateController reservationUpdateController;

    @InjectMocks
    private MemberServiceImpl memberService;

    private UUID appointmentId;
    private UUID purchaseId;
    private Appointment appointment;
    private Purchase purchase;

    @BeforeEach
    void setUp() {
        appointmentId = UUID.randomUUID();
        purchaseId = UUID.randomUUID();

        appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setMaxCapacity(10);

        purchase = new Purchase();
        purchase.setId(purchaseId);
        purchase.setUsesLeft(5);
    }

    @Test
    void reserveAppointment_Success() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));
        when(reservationRepository.countByAppointment(appointment)).thenReturn(5L);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result = memberService.reserveAppointment(appointmentId, purchaseId);

        assertNotNull(result);
        assertEquals(appointment, result.getAppointment());
        assertEquals(purchase, result.getPurchase());
        assertEquals(4, purchase.getUsesLeft());

        verify(reservationRepository).save(any(Reservation.class));
        verify(purchaseRepository).save(purchase);
        verify(reservationUpdateController).sendUpdate(any(SeatUpdateDto.class));
    }

    @Test
    void reserveAppointment_AppointmentNotFound() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> memberService.reserveAppointment(appointmentId, purchaseId));
    }

    @Test
    void reserveAppointment_PurchaseNotFound() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> memberService.reserveAppointment(appointmentId, purchaseId));
    }

    @Test
    void reserveAppointment_NoUsesLeft() {
        purchase.setUsesLeft(0);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));

        assertThrows(RuntimeException.class, () -> memberService.reserveAppointment(appointmentId, purchaseId));
    }

    @Test
    void reserveAppointment_NoSeatsAvailable() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));
        when(reservationRepository.countByAppointment(appointment)).thenReturn(10L);

        assertThrows(RuntimeException.class, () -> memberService.reserveAppointment(appointmentId, purchaseId));
    }
}
