package com.flight_system.customer_profile.exceptions;

public class CustomerProfileNotFoundException extends RuntimeException {
    public CustomerProfileNotFoundException(String message) {
        super(message);
    }
}
