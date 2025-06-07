package com.flight_system.revenue_service.repository;

import com.flight_system.revenue_service.model.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long> {
    List<Revenue> findByRevenueDate(LocalDate date);
    List<Revenue> findByFlightNumber(String flightNumber);
} 