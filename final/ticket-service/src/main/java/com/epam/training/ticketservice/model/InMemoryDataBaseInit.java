package com.epam.training.ticketservice.model;

import com.epam.training.ticketservice.repositories.AccountRepository;
import com.epam.training.ticketservice.repositories.MovieRepository;
import com.epam.training.ticketservice.repositories.PriceComponentRepository;
import com.epam.training.ticketservice.repositories.RoomRepository;
import com.epam.training.ticketservice.repositories.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InMemoryDataBaseInit {

    private final AccountRepository accountRepository;

    @Value(value = "${init-admin}")
    private boolean initAdmin;

    @PostConstruct
    public void init() {

        if (initAdmin) {
            if (accountRepository.findByUsername("admin").isEmpty()) {
                Account admin = new Account("admin", "admin", AccountType.ADMIN);
                accountRepository.save(admin);
            }
        }
    }
}
