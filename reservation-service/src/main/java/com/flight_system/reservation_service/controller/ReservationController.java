package com.flight_system.reservation_service.controller;

import com.flight_system.reservation_service.model.Reservation;
import com.flight_system.reservation_service.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody Reservation reservation) {
        Reservation newReservation = reservationService.createReservation(reservation);
        return new ResponseEntity<>(newReservation, HttpStatus.CREATED);
    }

    // Get all reservations
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/{reservationNumber}")
    public ResponseEntity<Reservation> getReservation(@PathVariable String reservationNumber) {
        return ResponseEntity.ok(reservationService.findByReservationNumber(reservationNumber));
    }

    // Get reservations by customer ID
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Reservation>> getReservationsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(reservationService.findByCustomerId(customerId));
    }

    // Get reservations by flight number
    @GetMapping("/flight/{flightNumber}")
    public ResponseEntity<List<Reservation>> getReservationsByFlight(@PathVariable String flightNumber) {
        return ResponseEntity.ok(reservationService.findByFlightNumber(flightNumber));
    }

    // Get reservations by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Reservation>> getReservationsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(reservationService.findByStatus(status));
    }

    // Update reservation
    @PutMapping("/{reservationNumber}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable String reservationNumber, 
            @Valid @RequestBody Reservation reservation) {
        Reservation updatedReservation = reservationService.updateReservation(reservationNumber, reservation);
        return ResponseEntity.ok(updatedReservation);
    }
    
    @PostMapping("/{reservationNumber}/confirm")
    public ResponseEntity<Reservation> confirm(@PathVariable String reservationNumber) {
        return ResponseEntity.ok(reservationService.confirmReservation(reservationNumber));
    }

    @PostMapping("/{reservationNumber}/cancel")
    public ResponseEntity<Reservation> cancel(@PathVariable String reservationNumber) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationNumber));
    }

    // Delete reservation
    @DeleteMapping("/{reservationNumber}")
    public ResponseEntity<Void> deleteReservation(@PathVariable String reservationNumber) {
        reservationService.deleteReservation(reservationNumber);
        return ResponseEntity.noContent().build();
    }

    // Update seat count for a reservation
    @PutMapping("/{reservationNumber}/seats")
    public ResponseEntity<Reservation> updateSeatCount(
            @PathVariable String reservationNumber, 
            @RequestParam int numberOfSeats) {
        Reservation updatedReservation = reservationService.updateSeatCount(reservationNumber, numberOfSeats);
        return ResponseEntity.ok(updatedReservation);
    }

    // Search reservations by customer email (requires integration with customer service)
    @GetMapping("/search")
    public ResponseEntity<List<Reservation>> searchReservations(
            @RequestParam(required = false) String customerEmail,
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String status) {
        List<Reservation> reservations = reservationService.searchReservations(customerEmail, flightNumber, status);
        return ResponseEntity.ok(reservations);
    }
} 