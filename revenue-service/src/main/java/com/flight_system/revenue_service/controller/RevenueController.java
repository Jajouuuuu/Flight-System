package com.flight_system.revenue_service.controller;

import com.flight_system.revenue_service.exceptions.RevenueNotFoundException;
import com.flight_system.revenue_service.model.Revenue;
import com.flight_system.revenue_service.repository.RevenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/revenue")
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueRepository revenueRepository;

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Revenue>> getRevenueByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<Revenue> revenues = revenueRepository.findByRevenueDate(date);
        if (revenues.isEmpty()) {
            throw new RevenueNotFoundException("No revenue found for date: " + date);
        }
        return ResponseEntity.ok(revenues);
    }

    @GetMapping("/flight/{flightNumber}")
    public ResponseEntity<List<Revenue>> getRevenueByFlight(@PathVariable String flightNumber) {
        List<Revenue> revenues = revenueRepository.findByFlightNumber(flightNumber);
        if (revenues.isEmpty()) {
            throw new RevenueNotFoundException("No revenue found for flight: " + flightNumber);
        }
        return ResponseEntity.ok(revenues);
    }

    @GetMapping("/total/date/{date}")
    public ResponseEntity<Double> getTotalRevenueByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Double total = revenueRepository.getTotalRevenueByDate(date);
        return ResponseEntity.ok(total != null ? total : 0.0);
    }
}
