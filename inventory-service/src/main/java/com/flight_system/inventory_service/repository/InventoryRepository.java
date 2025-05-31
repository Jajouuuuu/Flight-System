package com.flight_system.inventory_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.flight_system.inventory_service.model.Inventory;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByFlightNumber(String flightNumber);
}

