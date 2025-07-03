package com.flight_system.payment_service.service;

import com.flight_system.payment_service.exceptions.PaymentNotFoundException;
import com.flight_system.payment_service.model.Payment;
import com.flight_system.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
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
                .paymentMethod("Credit Card")
                .status("PENDING")
                .build();

        paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public Payment processPayment(String bookingNumber) {
        Payment payment = paymentRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for booking: " + bookingNumber));

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

    @Override
    @Transactional
    public Payment getPaymentByBookingNumber(String bookingNumber) {
        return paymentRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for booking: " + bookingNumber));
    }

    @Override
    @Transactional
    public Payment createPayment(Payment payment) {
        log.info("Creating new payment for booking: {}", payment.getBookingNumber());
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    @Transactional
    public Payment updatePaymentStatus(Long paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + paymentId));
        
        payment.setStatus(status);
        if ("COMPLETED".equals(status)) {
            payment.setPaymentDate(LocalDateTime.now());
        }
        
        return paymentRepository.save(payment);
    }
}
