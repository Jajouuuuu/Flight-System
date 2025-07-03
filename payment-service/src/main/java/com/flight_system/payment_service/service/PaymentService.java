package com.flight_system.payment_service.service;

import com.flight_system.payment_service.model.Payment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PaymentService {
    void handleBookingConfirmed(Map<String, Object> bookingData);
    Payment processPayment(String bookingNumber);
    Payment getPaymentByBookingNumber(String bookingNumber);
    Payment createPayment(Payment payment);
    List<Payment> getAllPayments();
    Payment updatePaymentStatus(Long paymentId, String status);
} 
