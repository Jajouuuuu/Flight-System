package com.flight_system.inventory_service.repository;

import com.flight_system.inventory_service.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByInventoryFlightNumberAndAvailableTrue(String flightNumber);
    Optional<Seat> findBySeatNumberAndInventoryFlightNumber(String seatNumber, String flightNumber);
}

