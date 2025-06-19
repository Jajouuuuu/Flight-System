package com.flight_system.flight_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.flight_system.flight_service.model.Flight;
import com.flight_system.flight_service.service.FlightService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping
    public ResponseEntity<Flight> create(@RequestBody Flight flight) {
        return ResponseEntity.ok(flightService.createFlight(flight));
    }

    @GetMapping
    public ResponseEntity<List<Flight>> getAll() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getById(@PathVariable Long id) {
        return flightService.getFlightById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{flightNumber}")
    public ResponseEntity<Flight> getByNumber(@PathVariable String flightNumber) {
        return flightService.getFlightByNumber(flightNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flight> update(@PathVariable Long id, @RequestBody Flight flight) {
        return ResponseEntity.ok(flightService.updateFlight(id, flight));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Flight> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(flightService.updateFlightStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Flight>> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime) {
        return ResponseEntity.ok(flightService.searchFlights(origin, destination, departureTime));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Flight>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(flightService.getFlightsByStatus(status));
    }

    @GetMapping("/timerange")
    public ResponseEntity<List<Flight>> getInTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(flightService.getFlightsInTimeRange(start, end));
    }

    @PatchMapping("/{id}/seats")
    public ResponseEntity<Void> updateSeats(@PathVariable Long id, @RequestParam int change) {
        flightService.updateAvailableSeats(id, change);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/number/{flightNumber}/available")
    public ResponseEntity<Boolean> isFlightNumberAvailable(@PathVariable String flightNumber) {
        return ResponseEntity.ok(flightService.isFlightNumberAvailable(flightNumber));
    }
}

