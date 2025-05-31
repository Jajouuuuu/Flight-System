package com.flight_system.inventory_service.service;

import com.flight_system.inventory_service.model.Seat;

import java.util.List;

public interface SeatService {
    List<Seat> getAvailableSeats(String flightNumber);
    Seat reserveSeat(String flightNumber, String seatNumber);
}

