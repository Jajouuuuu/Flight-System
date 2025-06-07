package com.flight_system.flight_service.service;

import com.flight_system.flight_service.model.Flight;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightService {
    Flight createFlight(Flight flight);
    List<Flight> getAllFlights();
    Optional<Flight> getFlightById(Long id);
    Optional<Flight> getFlightByNumber(String flightNumber);
    void deleteFlight(Long id);
    Flight updateFlight(Long id, Flight updatedFlight);
    Flight updateFlightStatus(Long id, String status);
    List<Flight> searchFlights(String origin, String destination, LocalDateTime departureTime);
    List<Flight> getFlightsByStatus(String status);
    List<Flight> getFlightsInTimeRange(LocalDateTime start, LocalDateTime end);
    boolean isFlightNumberAvailable(String flightNumber);
    void updateAvailableSeats(Long id, int change);
}

