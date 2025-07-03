package com.flight_system.inventory_service.controller;

import com.flight_system.inventory_service.model.FlightInventory;
import com.flight_system.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    // POST endpoint to create new inventory
    @PostMapping
    public ResponseEntity<FlightInventory> createInventory(@RequestBody FlightInventory inventory) {
        FlightInventory createdInventory = inventoryService.createInventory(inventory);
        return new ResponseEntity<>(createdInventory, HttpStatus.CREATED);
    }

    // GET all inventories
    @GetMapping
    public ResponseEntity<List<FlightInventory>> getAllInventories() {
        return ResponseEntity.ok(inventoryService.getAllInventories());
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<FlightInventory> getInventory(@PathVariable Long flightId) {
        return ResponseEntity.ok(inventoryService.getInventoryByFlightId(flightId));
    }

    // GET inventory by flight number
    @GetMapping("/flight/number/{flightNumber}")
    public ResponseEntity<FlightInventory> getInventoryByFlightNumber(@PathVariable String flightNumber) {
        return ResponseEntity.ok(inventoryService.getInventoryByFlightNumber(flightNumber));
    }

    // PUT endpoint to update inventory
    @PutMapping("/{inventoryId}")
    public ResponseEntity<FlightInventory> updateInventory(
            @PathVariable Long inventoryId, 
            @RequestBody FlightInventory inventory) {
        FlightInventory updatedInventory = inventoryService.updateInventory(inventoryId, inventory);
        return ResponseEntity.ok(updatedInventory);
    }

    // DELETE endpoint to delete inventory
    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long inventoryId) {
        inventoryService.deleteInventory(inventoryId);
        return ResponseEntity.noContent().build();
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

    // GET available seats count for a flight
    @GetMapping("/flight/{flightNumber}/available-seats")
    public ResponseEntity<Integer> getAvailableSeats(@PathVariable String flightNumber) {
        return ResponseEntity.ok(inventoryService.getAvailableSeats(flightNumber));
    }
}

