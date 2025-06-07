package com.flight_system.payment_service.controller;

import com.flight_system.payment_service.model.Payment;
import com.flight_system.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/booking/{bookingNumber}/pay")
    public ResponseEntity<Payment> pay(@PathVariable String bookingNumber) {
        return ResponseEntity.ok(paymentService.processPayment(bookingNumber));
    }

    @GetMapping("/booking/{bookingNumber}")
    public ResponseEntity<Payment> getPayment(@PathVariable String bookingNumber) {
        return ResponseEntity.ok(paymentService.getPaymentByBookingNumber(bookingNumber));
    }
} 