package com.andrea.fitness.service;

import com.andrea.fitness.dto.createRequestDto.PaymentIntentCreateRequestDto;
import com.andrea.fitness.model.Purchase;
import com.andrea.fitness.model.PurchaseStatus;
import com.andrea.fitness.model.TrainingService;
import com.andrea.fitness.model.User;
import com.andrea.fitness.repository.PurchaseRepository;
import com.andrea.fitness.repository.TrainingServiceRepository;
import com.andrea.fitness.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
public class StripeServiceImpl implements StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    private final TrainingServiceRepository trainingServiceRepository;
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;

    public StripeServiceImpl(TrainingServiceRepository trainingServiceRepository, PurchaseRepository purchaseRepository, UserRepository userRepository) {
        this.trainingServiceRepository = trainingServiceRepository;
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        if (stripeApiKey == null || stripeApiKey.isBlank()) {
            log.warn("Stripe API key not configured (STRIPE_SECRET). Stripe operations will be disabled.");
            return;
        }
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentIntentCreateRequestDto request, User member) throws StripeException {
        TrainingService service = trainingServiceRepository.findById(request.serviceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        long amountInCents = service.getPrice().multiply(new BigDecimal("100")).longValue();

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amountInCents)
                        .setCurrency("eur")
                        .putMetadata("serviceId", service.getId().toString())
                        .putMetadata("memberId", member.getId().toString())
                        .build();

        return PaymentIntent.create(params);
    }

    @Override
    public void handleStripeEvent(String payload, String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;
            if (dataObjectDeserializer.getObject().isPresent()) {
                stripeObject = dataObjectDeserializer.getObject().get();
            } else {
                 log.warn("Could not deserialize event data object");
            }

            switch (event.getType()) {
                case "payment_intent.succeeded":
                    PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                    fulfillOrder(paymentIntent);
                    log.info("Payment for {} succeeded.", paymentIntent.getId());
                    break;
                case "payment_intent.payment_failed":
                    PaymentIntent paymentIntentFailed = (PaymentIntent) stripeObject;
                    log.error("Payment for {} failed.", paymentIntentFailed.getId());
                    break;
                default:
                    log.warn("Unhandled event type: {}", event.getType());
                    break;
            }
        } catch (SignatureVerificationException e) {
            log.error("Error handling Stripe webhook", e);
            throw new RuntimeException(e);
        }
    }

    private void fulfillOrder(PaymentIntent paymentIntent) {
        Map<String, String> metadata = paymentIntent.getMetadata();
        String serviceId = metadata.get("serviceId");
        String memberId = metadata.get("memberId");

        TrainingService service = trainingServiceRepository.findById(java.util.UUID.fromString(serviceId))
                .orElseThrow(() -> new RuntimeException("Service not found"));
        User member = userRepository.findById(java.util.UUID.fromString(memberId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        Purchase purchase = new Purchase();
        purchase.setMember(member);
        purchase.setService(service);
        purchase.setAmount(service.getPrice());
        purchase.setStripePaymentIntentId(paymentIntent.getId());
        purchase.setPurchasedAt(LocalDateTime.now());
        purchase.setStatus(PurchaseStatus.SUCCEEDED);

        purchaseRepository.save(purchase);
    }
}
