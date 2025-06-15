package com.flight_system.search_service.service;

import com.flight_system.search_service.dto.FlightSearchResultDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SearchService {
    List<FlightSearchResultDTO> searchFlights(String origin, String destination, LocalDateTime departureTime);
}
