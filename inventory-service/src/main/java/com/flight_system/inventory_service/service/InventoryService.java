package com.flight_system.inventory_service.service;

import com.flight_system.inventory_service.model.FlightInventory;
import java.util.List;
import java.util.Map;

public interface InventoryService {
    void handleFlightCreated(Map<String, Object> flightData);
    void handleFlightCancelled(Map<String, Object> flightData);
    FlightInventory getInventoryByFlightId(Long flightId);
    void reserveSeats(String flightNumber, int seatsToReserve);
    void releaseSeats(String flightNumber, int seatsToRelease);
    FlightInventory createInventory(FlightInventory inventory);
    List<FlightInventory> getAllInventories();
    FlightInventory getInventoryByFlightNumber(String flightNumber);
    FlightInventory updateInventory(Long inventoryId, FlightInventory inventory);
    void deleteInventory(Long inventoryId);
    Integer getAvailableSeats(String flightNumber);
}

