package com.epam.training.ticketservice.exceptions;

public class AccountLeftSignedIn extends Exception {
    public AccountLeftSignedIn(String message) {
        super(message);
    }
}
