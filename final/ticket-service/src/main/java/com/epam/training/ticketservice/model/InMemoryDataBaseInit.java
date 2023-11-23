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
    private final PriceComponentRepository priceComponentRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ScreeningRepository screeningRepository;

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
        //priceComponentRepository.save(new PriceComponent("additionalFeeForPedersoli", 100));
        //Movie movie = new Movie("Sátántangó", "drama", 450);
        //movieRepository.save(movie);
        //Room room = new Room("Pedersoli", 20, 10);
        //roomRepository.save(room);
        //screeningRepository.save(new Screening(movie, room, LocalDateTime.parse("2021-03-15 10:45",
        //        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
    }
}
