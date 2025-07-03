package com.flight_system.payment_service.controller;

import com.flight_system.payment_service.model.Payment;
import com.flight_system.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
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

    // Create payment manually
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Payment createdPayment = paymentService.createPayment(payment);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    // Get all payments
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    // Update payment status
    @PutMapping("/{paymentId}/status")
    public ResponseEntity<Payment> updatePaymentStatus(
            @PathVariable Long paymentId, 
            @RequestParam String status) {
        Payment updatedPayment = paymentService.updatePaymentStatus(paymentId, status);
        return ResponseEntity.ok(updatedPayment);
    }
} 