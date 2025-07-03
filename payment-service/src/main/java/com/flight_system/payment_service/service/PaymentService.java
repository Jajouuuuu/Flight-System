package com.flight_system.payment_service.service;

import com.flight_system.payment_service.model.Payment;
import java.time.LocalDateTime;
import java.util.List;
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
        Double amount = (Double) bookingData.get("totalPrice");

        Payment payment = Payment.builder()
                .bookingNumber(bookingNumber)
                .amount(amount)
                .paymentMethod("Credit Card") // Default payment method
                .status("PENDING")
                .build();
        
        paymentRepository.save(payment);
    }

    @Transactional
    public Payment processPayment(String bookingNumber) {
        Payment payment = paymentRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found for booking: " + bookingNumber));

        // Simulate payment processing
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setStatus("COMPLETED");
        payment.setPaymentDate(LocalDateTime.now());
        
        Payment savedPayment = paymentRepository.save(payment);

        kafkaTemplate.send("payment-completed", Map.of(
            "bookingNumber", savedPayment.getBookingNumber(),
            "transactionId", savedPayment.getTransactionId(),
            "status", savedPayment.getStatus()
        ));

        return savedPayment;
    }

    @Transactional(readOnly = true)
    public Payment getPaymentByBookingNumber(String bookingNumber) {
        return paymentRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found for booking: " + bookingNumber));
    }

    // Payment management methods
    @Transactional
    public Payment createPayment(Payment payment) {
        log.info("Creating new payment for booking: {}", payment.getBookingNumber());
        return paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Transactional
    public Payment updatePaymentStatus(Long paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + paymentId));
        
        payment.setStatus(status);
        if ("COMPLETED".equals(status)) {
            payment.setPaymentDate(LocalDateTime.now());
        }
        
        return paymentRepository.save(payment);
    }
} 
