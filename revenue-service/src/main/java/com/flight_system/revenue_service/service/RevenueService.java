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
    public void handlePaymentCompleted(Map<String, Object> event) {
        log.info("Received payment completed event: {}", event);

        String flightNumber = (String) event.get("flightNumber");
        Double amount = Double.valueOf(event.get("amount").toString());

        String paymentDateStr = (String) event.get("paymentDate");
        LocalDate revenueDate = paymentDateStr != null
                ? LocalDate.parse(paymentDateStr.substring(0, 10))
                : LocalDate.now(); // fallback

        revenueRepository.save(Revenue.builder()
                .flightNumber(flightNumber)
                .revenueDate(revenueDate)
                .amount(amount)
                .build());
    }

} 