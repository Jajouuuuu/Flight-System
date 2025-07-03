package com.flight_system.check_in.exceptions;

public class CheckInAlreadyExistsException extends RuntimeException {
    public CheckInAlreadyExistsException(String message) {
        super(message);
    }
}
