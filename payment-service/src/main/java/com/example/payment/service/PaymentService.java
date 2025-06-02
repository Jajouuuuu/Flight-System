package com.example.payment.service;

import com.example.payment.dto.PaymentRequest;
import com.example.payment.dto.PaymentResponse;
import java.util.List;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest paymentRequest);
    PaymentResponse getPaymentById(Long id);
    PaymentResponse getPaymentByReservationId(String reservationId);
    List<PaymentResponse> getAllPayments();
    PaymentResponse refundPayment(Long id);
} 