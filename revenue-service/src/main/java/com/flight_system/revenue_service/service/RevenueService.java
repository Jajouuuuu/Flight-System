package com.flight_system.revenue_service.service;

import com.flight_system.revenue_service.model.Revenue;
import com.flight_system.revenue_service.repository.RevenueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RevenueService {
    private final RevenueRepository revenueRepository;

    @KafkaListener(topics = "payment-completed", groupId = "revenue-service")
    public void handlePaymentCompleted(Map<String, Object> paymentData) {
        log.info("Received payment completed event: {}", paymentData);
        String flightNumber = (String) paymentData.get("flightNumber");
        Double amount = (Double) paymentData.get("amount");

        Revenue revenue = Revenue.builder()
                .flightNumber(flightNumber)
                .revenueDate(LocalDate.now())
                .amount(amount)
                .build();
        
        revenueRepository.save(revenue);
    }
} 