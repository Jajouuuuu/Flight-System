package com.flight_system.revenue_service.exceptions;

public class RevenueNotFoundException extends RuntimeException {
    public RevenueNotFoundException(String message) {
        super(message);
    }
}
