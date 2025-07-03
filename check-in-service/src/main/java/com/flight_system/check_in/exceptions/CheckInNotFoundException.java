package com.flight_system.check_in.exceptions;

public class CheckInNotFoundException extends RuntimeException {
    public CheckInNotFoundException(String message) {
        super(message);
    }
}
