package com.flight_system.check_in.dto;

import lombok.Data;

@Data
public class CheckInDTO {
    private Long bookingId;
    private Long customerId;
    private String seatNumber;
} 