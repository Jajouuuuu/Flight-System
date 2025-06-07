package com.flight_system.search_service.service;

import com.flight_system.search_service.client.FlightClient;
import com.flight_system.search_service.client.InventoryClient;
import com.flight_system.search_service.dto.FlightDTO;
import com.flight_system.search_service.dto.SearchResultDTO;
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
    public List<SearchResultDTO> search(String origin, String destination, LocalDate date) {
        List<FlightDTO> flights = flightClient.searchFlights(origin, destination, date);
        return flights.stream().map(flight -> {
            int availableSeats = inventoryClient.countAvailableSeats(
                    flight.getFlightNumber(),
                    flight.getDepartureTime().toString()
            );
            return new SearchResultDTO(
                    flight.getFlightNumber(),
                    flight.getDepartureTime(),
                    flight.getAircraft(),
                    availableSeats
            );
        }).collect(Collectors.toList());
    }
}
