package com.flight_system.inventory_service.service;

import com.flight_system.inventory_service.model.FlightInventory;
import java.util.Map;

public interface InventoryService {
    void handleFlightCreated(Map<String, Object> flightData);
    void handleFlightCancelled(Map<String, Object> flightData);
    FlightInventory getInventoryByFlightId(Long flightId);
    void reserveSeats(String flightNumber, int seatsToReserve);
    void releaseSeats(String flightNumber, int seatsToRelease);
}

