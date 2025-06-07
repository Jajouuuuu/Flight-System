package com.flight_system.flight_service.repository;

import com.flight_system.flight_service.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    Optional<Flight> findByFlightNumber(String flightNumber);

    boolean existsByFlightNumber(String flightNumber);

    List<Flight> findByStatus(String status);
    
    List<Flight> findByRouteOriginAirportAndRouteDestinationAirportAndDepartureTimeBetween(
            String origin, String destination, LocalDateTime start, LocalDateTime end);

    List<Flight> findByDepartureTimeBetween(LocalDateTime start, LocalDateTime end);
}

