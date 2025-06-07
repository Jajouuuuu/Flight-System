package com.flight_system.flight_service.service;

import com.flight_system.flight_service.model.Flight;

import java.util.List;
import java.util.Optional;

public interface FlightService {
    Flight createFlight(Flight flight);
    List<Flight> getAllFlights();
    Optional<Flight> getFlightById(Long id);
    void deleteFlight(Long id);
    Flight updateFlight(Long id, Flight updatedFlight);
}

