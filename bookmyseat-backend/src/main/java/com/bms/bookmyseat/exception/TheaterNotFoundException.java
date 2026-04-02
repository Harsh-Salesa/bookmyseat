package com.bms.bookmyseat.exception;

public class TheaterNotFoundException extends RuntimeException {
    public TheaterNotFoundException(String message) {
        super(message);
    }
}
