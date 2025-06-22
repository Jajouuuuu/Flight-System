package com.flight_system.payment_service.service;

import com.flight_system.payment_service.exceptions.PaymentNotFoundException;
import com.flight_system.payment_service.model.Payment;
import com.flight_system.payment_service.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "booking-confirmed", groupId = "payment-service")
    @Transactional
    public void handleBookingConfirmed(Map<String, Object> bookingData) {
        log.info("Received booking confirmed event: {}", bookingData);
        String bookingNumber = (String) bookingData.get("bookingNumber");
        String flightNumber = (String) bookingData.get("flightNumber");
        Double amount = (Double) bookingData.get("totalPrice");

        Payment payment = Payment.builder()
                .bookingNumber(bookingNumber)
                .flightNumber(flightNumber)
                .amount(amount)
                .paymentMethod("Credit Card") // Default payment method
                .status("PENDING")
                .build();
        
        paymentRepository.save(payment);
    }

    @Transactional
    public Payment processPayment(String bookingNumber) {
        Payment payment = paymentRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for booking: " + bookingNumber));

        // Simulate payment processing
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setStatus("COMPLETED");
        payment.setPaymentDate(LocalDateTime.now());
        
        Payment savedPayment = paymentRepository.save(payment);

        kafkaTemplate.send("payment-completed", Map.of(
                "bookingNumber", savedPayment.getBookingNumber(),
                "transactionId", savedPayment.getTransactionId(),
                "status", savedPayment.getStatus(),
                "amount", savedPayment.getAmount(),
                "flightNumber", savedPayment.getFlightNumber(),
                "paymentDate", savedPayment.getPaymentDate().toString()
        ));


        return savedPayment;
    }

    @Transactional(readOnly = true)
    public Payment getPaymentByBookingNumber(String bookingNumber) {
        return paymentRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for booking: " + bookingNumber));
    }
} 