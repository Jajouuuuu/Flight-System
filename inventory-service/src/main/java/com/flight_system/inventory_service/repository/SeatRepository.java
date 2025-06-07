package com.flight_system.inventory_service.repository;

import com.flight_system.inventory_service.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByInventoryFlightNumberAndAvailableTrue(String flightNumber);
    Optional<Seat> findBySeatNumberAndInventoryFlightNumber(String seatNumber, String flightNumber);
    @Query("SELECT COUNT(s) FROM Seat s WHERE s.inventory.flightNumber = :flightNumber AND s.inventory.departureDateTime = :departureDateTime AND s.available = true")
    int countAvailableSeats(@Param("flightNumber") String flightNumber, @Param("departureDateTime") LocalDateTime departureDateTime);
}

