package com.flight_system.flight_service.controller;

import com.flight_system.flight_service.exceptions.FlightNotFoundException;
import com.flight_system.flight_service.model.Flight;
import com.flight_system.flight_service.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
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
        Flight flight = flightService.getFlightById(id)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + id));
        return ResponseEntity.ok(flight);
    }

    @GetMapping("/number/{flightNumber}")
    public ResponseEntity<Flight> getByNumber(@PathVariable String flightNumber) {
        Flight flight = flightService.getFlightByNumber(flightNumber)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with number: " + flightNumber));
        return ResponseEntity.ok(flight);
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
        List<Flight> flights = flightService.searchFlights(origin, destination, departureTime);
        if (flights.isEmpty()) {
            throw new FlightNotFoundException("No flights found from " + origin + " to " + destination + " at " + departureTime);
        }
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Flight>> getByStatus(@PathVariable String status) {
        List<Flight> flights = flightService.getFlightsByStatus(status);
        if (flights.isEmpty()) {
            throw new FlightNotFoundException("No flights found with status: " + status);
        }
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/timerange")
    public ResponseEntity<List<Flight>> getInTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Flight> flights = flightService.getFlightsInTimeRange(start, end);
        if (flights.isEmpty()) {
            throw new FlightNotFoundException("No flights found between " + start + " and " + end);
        }
        return ResponseEntity.ok(flights);
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
