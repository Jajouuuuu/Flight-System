package com.flight_system.flight_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.flight_system.flight_service.model.Flight;
import com.flight_system.flight_service.repository.FlightRepository;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository repository;

    @Override
    @Transactional
    public Flight createFlight(Flight flight) {
        if (repository.existsByFlightNumber(flight.getFlightNumber())) {
            throw new IllegalArgumentException("Flight number already exists: " + flight.getFlightNumber());
        }
        return repository.save(flight);
    }

    @Override
    public List<Flight> getAllFlights() {
        return repository.findAll();
    }

    @Override
    public Optional<Flight> getFlightById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Flight> getFlightByNumber(String flightNumber) {
        return repository.findByFlightNumber(flightNumber);
    }

    @Override
    @Transactional
    public void deleteFlight(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Flight not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Flight updateFlight(Long id, Flight updatedFlight) {
        return repository.findById(id).map(flight -> {
            flight.setFlightNumber(updatedFlight.getFlightNumber());
            flight.setDepartureTime(updatedFlight.getDepartureTime());
            flight.setArrivalTime(updatedFlight.getArrivalTime());
            flight.setAvailableSeats(updatedFlight.getAvailableSeats());
            return repository.save(flight);
        }).orElseThrow(() -> new EntityNotFoundException("Flight not found with id: " + id));
    }

    @Override
    @Transactional
    public Flight updateFlightStatus(Long id, String status) {
        return repository.findById(id).map(flight -> {
            flight.setStatus(status);
            return repository.save(flight);
        }).orElseThrow(() -> new EntityNotFoundException("Flight not found with id: " + id));
    }

    @Override
    public List<Flight> searchFlights(String origin, String destination, LocalDateTime departureTime) {
        return repository.findByRouteOriginAirportAndRouteDestinationAirportAndDepartureTimeBetween(
                origin,
                destination,
                departureTime.toLocalDate().atStartOfDay(),
                departureTime.toLocalDate().plusDays(1).atStartOfDay()
        );
    }

    @Override
    public List<Flight> getFlightsByStatus(String status) {
        return repository.findByStatus(status);
    }

    @Override
    public List<Flight> getFlightsInTimeRange(LocalDateTime start, LocalDateTime end) {
        return repository.findByDepartureTimeBetween(start, end);
    }

    @Override
    public boolean isFlightNumberAvailable(String flightNumber) {
        return !repository.existsByFlightNumber(flightNumber);
    }

    @Override
    @Transactional
    public void updateAvailableSeats(Long id, int change) {
        repository.findById(id).ifPresent(flight -> {
            int newSeats = flight.getAvailableSeats() + change;
            if (newSeats < 0) {
                throw new IllegalStateException("Not enough available seats");
            }
            flight.setAvailableSeats(newSeats);
            repository.save(flight);
        });
    }
}

