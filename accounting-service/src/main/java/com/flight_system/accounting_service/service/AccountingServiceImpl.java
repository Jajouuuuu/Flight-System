package com.flight_system.accounting_service.service;

import com.flight_system.accounting_service.exceptions.InvalidPaymentDataException;
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
public class AccountingServiceImpl {
    private final AccountTransactionRepository transactionRepository;

    @KafkaListener(topics = "payment-completed", groupId = "accounting-service")
    public void handlePaymentCompleted(Map<String, Object> paymentData) {
        log.info("Received payment completed event for accounting: {}", paymentData);

        String bookingNumber = (String) paymentData.get("bookingNumber");
        Double amount = (Double) paymentData.get("amount");

        if (bookingNumber == null || amount == null) {
            throw new InvalidPaymentDataException("bookingNumber or amount is missing");
        }

        AccountTransaction transaction = AccountTransaction.builder()
                .referenceId(bookingNumber)
                .amount(amount)
                .transactionType("PAYMENT")
                .build();

        transactionRepository.save(transaction);
    }
} 