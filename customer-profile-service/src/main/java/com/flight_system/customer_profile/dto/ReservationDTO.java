package com.flight_system.customer_profile.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationDTO {
    private Long id;
    private Long flightId;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String status;
} 