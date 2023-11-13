package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.AccountType;
import com.epam.training.ticketservice.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private AccountDto loggedAcc = null;
    @Override
    public Optional<AccountDto> signIn(String username, String password) {
        Optional<Account> acc = accountRepository.findByUsername(username);
        if (acc.isEmpty()) {
            return Optional.empty();
        }
        if (AccountType.USER == acc.get().getAccountType() && acc.get().getPassword().equals(password)) {
            loggedAcc = new AccountDto(acc.get().getUsername(), acc.get().getAccountType());
        }
        else {
            return Optional.empty();
        }
        return describe();
    }

    @Override
    public Optional<AccountDto> signInPrivileged(String username, String password) {
        Optional<Account> acc = accountRepository.findByUsername(username);
        if (acc.isEmpty()) {
            return Optional.empty();
        }
        if (AccountType.ADMIN == acc.get().getAccountType() && acc.get().getPassword().equals(password)) {
            loggedAcc = new AccountDto(acc.get().getUsername(), acc.get().getAccountType());
        }
        else {
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
    public void signUp(String username, String password) {
        Account account = new Account(username, password, AccountType.USER);
        accountRepository.save(account);
    }
}
