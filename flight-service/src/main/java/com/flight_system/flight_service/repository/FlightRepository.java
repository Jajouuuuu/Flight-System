package com.flight_system.flight_service.repository;

import com.flight_system.flight_service.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByOriginAndDestination(String origin, String destination); // La fonction sera utile lors de la création de notre service Search
}

