package com.flight_system.search_service.service;

import com.flight_system.search_service.dto.FlightSearchResultDTO;
import com.flight_system.search_service.feign.FlightClient;
import com.flight_system.search_service.feign.InventoryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final FlightClient flightClient;
    private final InventoryClient inventoryClient;

    @Override
    public List<FlightSearchResultDTO> searchFlights(String origin, String destination, LocalDate departureDate) {
        // 1. Search for flights
        return flightClient.searchFlights(origin, destination, departureDate).stream()
                // 2. For each flight, get inventory
                .map(flight -> {
                    try {
                        int availableSeats = inventoryClient.getInventoryByFlightId(flight.getId()).getAvailableSeats();
                        return new FlightSearchResultDTO(
                                flight.getId(),
                                flight.getFlightNumber(),
                                flight.getOrigin(),
                                flight.getDestination(),
                                flight.getDepartureTime(),
                                flight.getArrivalTime(),
                                flight.getPrice(),
                                availableSeats
                        );
                    } catch (Exception e) {
                        // If inventory is not found, treat as 0 seats available
                        return new FlightSearchResultDTO(
                                flight.getId(),
                                flight.getFlightNumber(),
                                flight.getOrigin(),
                                flight.getDestination(),
                                flight.getDepartureTime(),
                                flight.getArrivalTime(),
                                flight.getPrice(),
                                0
                        );
                    }
                })
                .collect(Collectors.toList());
    }
}
