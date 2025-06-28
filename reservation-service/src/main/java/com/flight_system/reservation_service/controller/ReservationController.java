package com.flight_system.reservation_service.controller;

import com.flight_system.reservation_service.model.Reservation;
import com.flight_system.reservation_service.service.ReservationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationServiceImpl reservationService;

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody Reservation reservation) {
        Reservation newReservation = reservationService.createReservation(reservation);
        return new ResponseEntity<>(newReservation, HttpStatus.CREATED);
    }

    @GetMapping("/{reservationNumber}")
    public ResponseEntity<Reservation> getReservation(@PathVariable String reservationNumber) {
        return ResponseEntity.ok(reservationService.findByReservationNumber(reservationNumber));
    }
    
    @PostMapping("/{reservationNumber}/confirm")
    public ResponseEntity<Reservation> confirm(@PathVariable String reservationNumber) {
        return ResponseEntity.ok(reservationService.confirmReservation(reservationNumber));
    }

    @PostMapping("/{reservationNumber}/cancel")
    public ResponseEntity<Reservation> cancel(@PathVariable String reservationNumber) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationNumber));
    }
} 