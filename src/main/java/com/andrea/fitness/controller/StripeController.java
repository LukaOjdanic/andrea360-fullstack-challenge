package com.andrea.fitness.controller;

import com.andrea.fitness.dto.createRequestDto.PaymentIntentCreateRequestDto;
import com.andrea.fitness.dto.entityDto.PaymentIntentDto;
import com.andrea.fitness.model.User;
import com.andrea.fitness.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
@Tag(name = "Stripe", description = "Stripe payment APIs")
public class StripeController {

    private final StripeService stripeService;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/member/create-payment-intent")
    @Operation(summary = "Create a new payment intent")
    public ResponseEntity<PaymentIntentDto> createPaymentIntent(@RequestBody @Valid PaymentIntentCreateRequestDto request,
                                                                  @AuthenticationPrincipal User member) {
        try {
            PaymentIntent paymentIntent = stripeService.createPaymentIntent(request, member);
            return ResponseEntity.ok(new PaymentIntentDto(paymentIntent.getClientSecret()));
        } catch (StripeException e) {
            log.error("Error creating payment intent", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/public/stripe/webhook")
    @Operation(summary = "Handle Stripe webhook events")
    public ResponseEntity<Void> handleStripeWebhook(@RequestBody String payload,
                                                  @RequestHeader("Stripe-Signature") String sigHeader) {
        stripeService.handleStripeEvent(payload, sigHeader);
        return ResponseEntity.ok().build();
    }
}
