package com.flight_system.search_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlightDTO {
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private String aircraft;
}
