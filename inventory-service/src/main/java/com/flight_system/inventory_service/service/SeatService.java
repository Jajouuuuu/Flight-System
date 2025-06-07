package com.flight_system.inventory_service.service;

import com.flight_system.inventory_service.model.Seat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SeatService {
    List<Seat> getAvailableSeats(String flightNumber);
    Seat reserveSeat(String flightNumber, String seatNumber);
    int countAvailableSeats(@Param("flightNumber") String flightNumber, @Param("departureDateTime") LocalDateTime departureDateTime);

}

