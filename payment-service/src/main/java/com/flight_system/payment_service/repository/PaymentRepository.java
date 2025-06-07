package com.flight_system.payment_service.repository;

import com.flight_system.payment_service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBookingNumber(String bookingNumber);
    Optional<Payment> findByTransactionId(String transactionId);
} 