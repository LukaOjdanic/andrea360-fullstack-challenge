package com.andrea.fitness.service;

import com.andrea.fitness.dto.createRequestDto.PaymentIntentCreateRequestDto;
import com.andrea.fitness.model.User;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface StripeService {
    PaymentIntent createPaymentIntent(PaymentIntentCreateRequestDto request, User member) throws StripeException;
    void handleStripeEvent(String payload, String sigHeader);
}
