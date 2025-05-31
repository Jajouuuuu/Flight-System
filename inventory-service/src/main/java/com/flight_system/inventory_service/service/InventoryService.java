package com.flight_system.inventory_service.service;

import com.flight_system.inventory_service.model.Inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryService {
    Inventory createInventory(Inventory inventory);
    List<Inventory> getAll();
    Optional<Inventory> getByFlightNumber(String flightNumber);
    Inventory updateInventory(String flightNumber, int reservedSeats);
}

