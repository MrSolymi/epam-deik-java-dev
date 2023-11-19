package com.epam.training.ticketservice.exceptions;

public class SeatAlreadyTakenException extends Exception {
    public SeatAlreadyTakenException(String message) {
        super(message);
    }
}
