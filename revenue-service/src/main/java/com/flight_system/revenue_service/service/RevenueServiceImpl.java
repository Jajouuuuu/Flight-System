package com.flight_system.revenue_service.service;

import com.flight_system.revenue_service.exceptions.RevenueNotFoundException;
import com.flight_system.revenue_service.model.Revenue;
import com.flight_system.revenue_service.repository.RevenueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RevenueServiceImpl implements RevenueService {
    
    private final RevenueRepository revenueRepository;

    @Override
    @KafkaListener(topics = "payment-completed", groupId = "revenue-service")
    @Transactional
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

    @Override
    @Transactional(readOnly = true)
    public List<Revenue> getAllRevenue() {
        return revenueRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Revenue getRevenueById(Long id) {
        return revenueRepository.findById(id)
                .orElseThrow(() -> new RevenueNotFoundException("Revenue not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Revenue> getRevenueByDateRange(LocalDate startDate, LocalDate endDate) {
        return revenueRepository.findAll().stream()
                .filter(revenue -> !revenue.getRevenueDate().isBefore(startDate) && 
                                   !revenue.getRevenueDate().isAfter(endDate))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalRevenue() {
        return revenueRepository.findAll().stream()
                .mapToDouble(Revenue::getAmount)
                .sum();
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalRevenueByDateRange(LocalDate startDate, LocalDate endDate) {
        return getRevenueByDateRange(startDate, endDate).stream()
                .mapToDouble(Revenue::getAmount)
                .sum();
    }

    @Override
    @Transactional
    public Revenue createRevenue(Revenue revenue) {
        log.info("Creating new revenue record for flight: {}", revenue.getFlightNumber());
        return revenueRepository.save(revenue);
    }

    @Override
    @Transactional
    public Revenue updateRevenue(Long id, Revenue revenue) {
        Revenue existingRevenue = revenueRepository.findById(id)
                .orElseThrow(() -> new RevenueNotFoundException("Revenue not found with id: " + id));

        existingRevenue.setFlightNumber(revenue.getFlightNumber());
        existingRevenue.setAmount(revenue.getAmount());
        existingRevenue.setRevenueDate(revenue.getRevenueDate());

        return revenueRepository.save(existingRevenue);
    }

    @Override
    @Transactional
    public void deleteRevenue(Long id) {
        if (!revenueRepository.existsById(id)) {
            throw new RevenueNotFoundException("Revenue not found with id: " + id);
        }
        revenueRepository.deleteById(id);
    }
} 