package com.epam.training.ticketservice.model;

import com.epam.training.ticketservice.repositories.AccountRepository;
import com.epam.training.ticketservice.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InMemoryDataBaseInit {

    private final AccountRepository accountRepository;
    private final MovieRepository movieRepository;

    @Value(value = "${init-admin}")
    private boolean initAdmin;

    @PostConstruct
    public void init(){

        if (initAdmin) {
            if (accountRepository.findByUsername("admin").isEmpty()){
                Account admin = new Account("admin", "admin", AccountType.ADMIN);
                accountRepository.save(admin);
            }
        }
    }
}
