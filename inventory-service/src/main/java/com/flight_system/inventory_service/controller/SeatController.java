package com.flight_system.inventory_service.controller;

import com.flight_system.inventory_service.model.Seat;
import com.flight_system.inventory_service.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/{flightNumber}/available")
    public List<Seat> getAvailableSeats(@PathVariable String flightNumber) {
        return seatService.getAvailableSeats(flightNumber);
    }

    @PutMapping("/{flightNumber}/reserve/{seatNumber}")
    public Seat reserveSeat(@PathVariable String flightNumber, @PathVariable String seatNumber) {
        return seatService.reserveSeat(flightNumber, seatNumber);
    }

    @GetMapping("/{flightNumber}/{departure}/available/count")
    public int getAvailableCount(@PathVariable String flightNumber, @PathVariable String departure) {
        LocalDateTime departureDateTime = LocalDateTime.parse(departure);
        return seatService.countAvailableSeats(flightNumber, departureDateTime);
    }



}

