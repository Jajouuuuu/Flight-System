package com.flight_system.reservation_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service", path = "/api/inventory")
public interface InventoryClient {

    @PostMapping("/flight/{flightNumber}/reserve")
    ResponseEntity<Void> reserveSeats(@PathVariable String flightNumber, @RequestParam int seats);

    @PostMapping("/flight/{flightNumber}/release")
    ResponseEntity<Void> releaseSeats(@PathVariable String flightNumber, @RequestParam int seats);
} 