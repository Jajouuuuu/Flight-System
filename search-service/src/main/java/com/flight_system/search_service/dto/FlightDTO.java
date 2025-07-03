package com.flight_system.search_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FlightDTO {
    private Long id;
    private String flightNumber;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double basePrice;
    private int availableSeats;

    private RouteDTO route;
    private AircraftDTO aircraft;

    @Data
    public static class RouteDTO {
        private String originAirport;
        private String destinationAirport;
        private double basePrice;
    }

    @Data
    public static class AircraftDTO {
        private String registrationNumber;
        private String model;
        private String manufacturer;
    }
}
