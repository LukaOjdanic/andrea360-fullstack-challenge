package com.andrea.fitness.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andrea.fitness.dto.createRequestDto.ReservationCreateRequestDto;
import com.andrea.fitness.dto.entityDto.PurchaseDto;
import com.andrea.fitness.dto.entityDto.ReservationDto;
import com.andrea.fitness.model.PurchaseStatus;
import com.andrea.fitness.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MEMBER')")
@Tag(name = "Member", description = "Member management APIs")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/purchase/confirm")
    @Operation(summary = "Confirm a purchase")
    public ResponseEntity<PurchaseDto> confirmPurchase(@RequestParam String paymentIntentId) {
        var purchase = memberService.confirmPurchase(paymentIntentId);
        return ResponseEntity.ok(new PurchaseDto(
            purchase.getId(),
            purchase.getService().getId(),
            purchase.getService().getName(),
            purchase.getAmount(),
            PurchaseStatus.valueOf(purchase.getStatus().name()),
            purchase.getPurchasedAt()
        ));
    }

    @GetMapping("/purchases/available")
    @Operation(summary = "Get all available purchases")
    public ResponseEntity<List<PurchaseDto>> getAvailablePurchases() {
        return ResponseEntity.ok(
            memberService.getAvailablePurchases().stream()
                .map(p -> new PurchaseDto(
                    p.getId(),
                    p.getService().getId(),
                    p.getService().getName(),
                    p.getAmount(),
                    PurchaseStatus.valueOf(p.getStatus().name()),
                    p.getPurchasedAt()
                ))
                .toList()
        );
    }

    @PostMapping("/reserve")
    @Operation(summary = "Reserve an appointment")
    public ResponseEntity<ReservationDto> reserveAppointment(@Valid @RequestBody ReservationCreateRequestDto request) {
        var reservation = memberService.reserveAppointment(request.appointmentId(), request.purchaseId());
        return ResponseEntity.ok(new ReservationDto(
            reservation.getId(),
            reservation.getMember().getId(),
            reservation.getMember().getFirstName() + " " + reservation.getMember().getLastName(),
            reservation.getAppointment().getId(),
            reservation.getReservedAt(),
            reservation.getAppointment().getService().getName(),
            reservation.getAppointment().getStartTime()
        ));
    }

    @GetMapping("/reservations")
    @Operation(summary = "Get all reservations for the current member")
    public ResponseEntity<List<ReservationDto>> getMyReservations() {
        return ResponseEntity.ok(
            memberService.getMyReservations().stream()
                .map(r -> new ReservationDto(
                    r.getId(),
                    r.getMember().getId(),
                    r.getMember().getFirstName() + " " + r.getMember().getLastName(),
                    r.getAppointment().getId(),
                    r.getReservedAt(),
                    r.getAppointment().getService().getName(),
                    r.getAppointment().getStartTime()
                ))
                .toList()
        );
    }

    @GetMapping("/can-reserve/{appointmentId}")
    @Operation(summary = "Check if the current member can reserve an appointment")
    public ResponseEntity<Boolean> canReserve(@PathVariable UUID appointmentId) {
        return ResponseEntity.ok(memberService.canReserve(appointmentId));
    }

}