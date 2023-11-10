package com.epam.training.ticketservice.exceptions;

public class AlreadyExistsException extends Exception{
    public AlreadyExistsException(String message) {
        super(message);
    }
}
