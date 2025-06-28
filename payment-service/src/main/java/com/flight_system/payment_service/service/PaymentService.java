package com.flight_system.payment_service.service;

import com.flight_system.payment_service.model.Payment;

public interface PaymentService {
    Payment processPayment(String bookingNumber);
    Payment getPaymentByBookingNumber(String bookingNumber);
    void handleBookingConfirmed(java.util.Map<String, Object> bookingData);
}
