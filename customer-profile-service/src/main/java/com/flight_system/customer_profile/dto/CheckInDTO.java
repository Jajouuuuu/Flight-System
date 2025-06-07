package com.flight_system.customer_profile.dto;

import lombok.Data;

@Data
public class CheckInDTO {
    private Long id;
    private Long bookingId;
    private String seatNumber;
} 