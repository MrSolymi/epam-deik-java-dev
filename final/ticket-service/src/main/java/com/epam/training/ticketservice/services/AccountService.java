package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.exceptions.AccountLeftSignedIn;
import com.epam.training.ticketservice.exceptions.AlreadyExistsException;

import java.util.Optional;

public interface AccountService {
    Optional<AccountDto> signIn(String username, String password) throws AccountLeftSignedIn;

    Optional<AccountDto> signInPrivileged(String username, String password) throws AccountLeftSignedIn;

    Optional<AccountDto> logOut();

    Optional<AccountDto> describe();

    void signUp(String username, String password) throws AlreadyExistsException;
}
