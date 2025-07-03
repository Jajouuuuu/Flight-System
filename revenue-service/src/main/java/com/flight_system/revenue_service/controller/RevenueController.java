package com.flight_system.revenue_service.controller;

import com.flight_system.revenue_service.exceptions.RevenueNotFoundException;
import com.flight_system.revenue_service.model.Revenue;
import com.flight_system.revenue_service.repository.RevenueRepository;
import com.flight_system.revenue_service.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/revenue")
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping
    public ResponseEntity<List<Revenue>> getAllRevenue() {
        return ResponseEntity.ok(revenueService.getAllRevenue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Revenue> getRevenueById(@PathVariable Long id) {
        return ResponseEntity.ok(revenueService.getRevenueById(id));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Revenue>> getRevenueByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(revenueService.getRevenueByDateRange(startDate, endDate));
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getTotalRevenue() {
        return ResponseEntity.ok(revenueService.getTotalRevenue());
    }

    @GetMapping("/total/date-range")
    public ResponseEntity<Double> getTotalRevenueByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(revenueService.getTotalRevenueByDateRange(startDate, endDate));
    }

    @PostMapping
    public ResponseEntity<Revenue> createRevenue(@RequestBody Revenue revenue) {
        return ResponseEntity.ok(revenueService.createRevenue(revenue));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Revenue> updateRevenue(@PathVariable Long id, @RequestBody Revenue revenue) {
        return ResponseEntity.ok(revenueService.updateRevenue(id, revenue));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRevenue(@PathVariable Long id) {
        revenueService.deleteRevenue(id);
        return ResponseEntity.ok().build();
    }
} 
