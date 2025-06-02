package com.example.reservation.controller;

import com.example.reservation.dto.ReservationRequest;
import com.example.reservation.model.Reservation;
import com.example.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation createReservation(@Valid @RequestBody ReservationRequest request) {
        return service.createReservation(request);
    }

    @GetMapping("/{id}")
    public Reservation getReservation(@PathVariable Long id) {
        return service.getReservation(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<Reservation> getReservationsByCustomer(@PathVariable String customerId) {
        return service.getReservationsByCustomer(customerId);
    }

    @GetMapping("/flight/{flightId}")
    public List<Reservation> getReservationsByFlight(@PathVariable String flightId) {
        return service.getReservationsByFlight(flightId);
    }

    @PostMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelReservation(@PathVariable Long id) {
        service.cancelReservation(id);
    }
} 