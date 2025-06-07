package com.flight_system.inventory_service.repository;

import com.flight_system.inventory_service.model.FlightInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightInventoryRepository extends JpaRepository<FlightInventory, Long> {
    Optional<FlightInventory> findByFlightId(Long flightId);
    Optional<FlightInventory> findByFlightNumber(String flightNumber);
} 