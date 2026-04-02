package com.bms.bookmyseat.exception;

public class TheaterAlreadyExistsException extends RuntimeException {
    public TheaterAlreadyExistsException(String message) {
        super(message);
    }
}
