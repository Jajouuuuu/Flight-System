package com.flight_system.accounting_service.exceptions;

public class AccountingException extends RuntimeException {
    public AccountingException(String message) {
        super(message);
    }
}
