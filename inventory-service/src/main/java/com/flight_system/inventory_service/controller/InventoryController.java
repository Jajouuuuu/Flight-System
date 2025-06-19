package com.flight_system.inventory_service.controller;

import com.flight_system.inventory_service.model.FlightInventory;
import com.flight_system.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<FlightInventory> getInventory(@PathVariable Long flightId) {
        return ResponseEntity.ok(inventoryService.getInventoryByFlightId(flightId));
    }

    @PostMapping("/flight/{flightNumber}/reserve")
    public ResponseEntity<Void> reserveSeats(@PathVariable String flightNumber, @RequestParam int seats) {
        inventoryService.reserveSeats(flightNumber, seats);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/flight/{flightNumber}/release")
    public ResponseEntity<Void> releaseSeats(@PathVariable String flightNumber, @RequestParam int seats) {
        inventoryService.releaseSeats(flightNumber, seats);
        return ResponseEntity.ok().build();
    }
}

