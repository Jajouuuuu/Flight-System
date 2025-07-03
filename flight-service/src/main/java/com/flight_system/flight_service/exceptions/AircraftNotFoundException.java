package com.flight_system.flight_service.exceptions;

public class AircraftNotFoundException extends RuntimeException {
    public AircraftNotFoundException(String message) {
        super(message);
    }
}
