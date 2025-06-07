package com.flight_system.accounting_service.service;

import com.flight_system.accounting_service.model.AccountTransaction;
import com.flight_system.accounting_service.repository.AccountTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountingService {
    private final AccountTransactionRepository transactionRepository;

    @KafkaListener(topics = "payment-completed", groupId = "accounting-service")
    public void handlePaymentCompleted(Map<String, Object> paymentData) {
        log.info("Received payment completed event for accounting: {}", paymentData);
        String bookingNumber = (String) paymentData.get("bookingNumber");
        Double amount = (Double) paymentData.get("amount");

        // Create a debit entry for cash/accounts receivable
        AccountTransaction debitEntry = AccountTransaction.builder()
                .description("Payment received for booking " + bookingNumber)
                .debit(amount)
                .credit(0.0)
                .referenceId(bookingNumber)
                .build();
        transactionRepository.save(debitEntry);

        // Create a credit entry for revenue
        AccountTransaction creditEntry = AccountTransaction.builder()
                .description("Revenue recognized for booking " + bookingNumber)
                .debit(0.0)
                .credit(amount)
                .referenceId(bookingNumber)
                .build();
        transactionRepository.save(creditEntry);
    }
} 