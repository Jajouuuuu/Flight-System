package com.flight_system.flight_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flight_system.flight_service.model.Flight;
import com.flight_system.flight_service.repository.FlightRepository;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository repository;

    public Flight createFlight(Flight flight) {
        return repository.save(flight);
    }

    public List<Flight> getAllFlights() {
        return repository.findAll();
    }

    public Optional<Flight> getFlightById(Long id) {
        return repository.findById(id);
    }

    public void deleteFlight(Long id) {
        repository.deleteById(id);
    }

    public Flight updateFlight(Long id, Flight updatedFlight) {
        return repository.findById(id).map(flight -> {
            flight.setFlightNumber(updatedFlight.getFlightNumber());
            flight.setOrigin(updatedFlight.getOrigin());
            flight.setDestination(updatedFlight.getDestination());
            flight.setDepartureTime(updatedFlight.getDepartureTime());
            flight.setAircraft(updatedFlight.getAircraft());
            return repository.save(flight);
        }).orElseThrow(() -> new RuntimeException("Vol non trouvé"));
    }
}

