package com.flight_system.search_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SearchResultDTO {
    private String flightNumber;
    private LocalDate departureDate;
    private String aircraft;
    private int availableSeats;
}
