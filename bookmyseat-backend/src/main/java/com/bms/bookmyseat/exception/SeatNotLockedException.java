package com.bms.bookmyseat.exception;

public class SeatNotLockedException extends RuntimeException {
    public SeatNotLockedException(String message) {
        super(message);
    }
}
