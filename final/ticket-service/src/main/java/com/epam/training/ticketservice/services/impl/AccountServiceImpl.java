package com.epam.training.ticketservice.services.impl;

import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.exceptions.AccountLeftSignedIn;
import com.epam.training.ticketservice.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.AccountType;
import com.epam.training.ticketservice.repositories.AccountRepository;
import com.epam.training.ticketservice.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private static final String USERNAME_ALREADY_EXIST = "The username with the given username is already exists";

    private final AccountRepository accountRepository;
    private AccountDto loggedAcc = null;

    @Override
    public Optional<AccountDto> signIn(String username, String password) throws AccountLeftSignedIn {
        if (loggedAcc != null) {
            throw new AccountLeftSignedIn("Sign out first to sign into an another account");
        }
        Optional<Account> user = accountRepository.findByUsername(username);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        if (AccountType.USER == user.get().getAccountType() && user.get().getPassword().equals(password)) {
            loggedAcc = new AccountDto(user.get().getUsername(), user.get().getAccountType());
        } else {
            return Optional.empty();
        }
        return describe();
    }

    @Override
    public Optional<AccountDto> signInPrivileged(String username, String password) throws AccountLeftSignedIn {
        if (loggedAcc != null) {
            throw new AccountLeftSignedIn("Sign out first to sign into an another account");
        }
        Optional<Account> acc = accountRepository.findByUsername(username);
        if (acc.isEmpty()) {
            return Optional.empty();
        }
        if (AccountType.ADMIN == acc.get().getAccountType() && acc.get().getPassword().equals(password)) {
            loggedAcc = new AccountDto(acc.get().getUsername(), acc.get().getAccountType());
        } else {
            return Optional.empty();
        }
        return describe();
    }

    @Override
    public Optional<AccountDto> logOut() {
        Optional<AccountDto> previouslyLoggedAcc = describe();
        loggedAcc = null;
        return previouslyLoggedAcc;
    }

    @Override
    public Optional<AccountDto> describe() {
        return Optional.ofNullable(loggedAcc);
    }

    @Override
    public void signUp(String username, String password) throws AlreadyExistsException {
        Account account = new Account(username, password, AccountType.USER);
        if (accountRepository.findByUsername(username).isPresent()) {
            throw new AlreadyExistsException(USERNAME_ALREADY_EXIST);
        } else {
            accountRepository.save(account);
        }

    }
}
