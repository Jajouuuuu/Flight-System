package com.flight_system.revenue_service.service;

import com.flight_system.revenue_service.model.Revenue;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RevenueService {
    void handlePaymentCompleted(Map<String, Object> event);
    List<Revenue> getAllRevenue();
    Revenue getRevenueById(Long id);
    List<Revenue> getRevenueByDateRange(LocalDate startDate, LocalDate endDate);
    Double getTotalRevenue();
    Double getTotalRevenueByDateRange(LocalDate startDate, LocalDate endDate);
    Revenue createRevenue(Revenue revenue);
    Revenue updateRevenue(Long id, Revenue revenue);
    void deleteRevenue(Long id);
} 