package com.epam.training.ticketservice.model;

import com.epam.training.ticketservice.repositories.AccountRepository;
import com.epam.training.ticketservice.repositories.MovieRepository;
import com.epam.training.ticketservice.repositories.RoomRepository;
import com.epam.training.ticketservice.repositories.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class InMemoryDataBaseInit {

    private final AccountRepository accountRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ScreeningRepository screeningRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Value("${init-admin}")
    private boolean initAdmin;
    @PostConstruct
    public void init(){

        if (initAdmin){
            Account admin = Account.builder()
                    .username("admin")
                    .password("admin")
                    .accountType(AccountType.ADMIN)
                    .build();
            accountRepository.save(admin);
        }

        Account user = Account.builder()
                .username("user")
                .password("user")
                .accountType(AccountType.USER)
                .build();
        accountRepository.save(user);

        Movie movie = new Movie("Sátántangó", "action", 120);
        movieRepository.save(movie);

        Room room = new Room("Pedersoli", 20, 15);
        roomRepository.save(room);

        Screening screening = new Screening(movie, room,  LocalDateTime.parse("2021-03-15 10:45", formatter));
        screeningRepository.save(screening);

        // book Sátántangó Pedersoli "2021-03-15 10:45" "5,5 5,6"
    }
}
