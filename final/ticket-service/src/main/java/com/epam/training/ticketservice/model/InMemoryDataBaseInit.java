package com.epam.training.ticketservice.model;

import com.epam.training.ticketservice.repositories.AccountRepository;
import com.epam.training.ticketservice.repositories.MovieRepository;
import com.epam.training.ticketservice.repositories.RoomRepository;
import com.epam.training.ticketservice.repositories.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Profile(value = "ci")
public class InMemoryDataBaseInit {

    private final AccountRepository accountRepository;
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

        Movie movie = new Movie("Sátántangó", "drama", 450);
        Movie movie1 = new Movie("Spirited Away", "aminmation", 125);
        Movie movie2 = new Movie("Pulp Fiction", "drama", 154);
        movieRepository.save(movie);
        movieRepository.save(movie1);
        movieRepository.save(movie2);
        Room room = new Room("Pedersoli", 20, 10);
        Room room1 = new Room("Girotti", 10, 10);
        roomRepository.save(room);
        roomRepository.save(room1);
        Screening screening = new Screening(movie, room, LocalDateTime.parse("2021-03-15 10:45",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Screening screening1 = new Screening(movie1, room, LocalDateTime.parse("2021-03-14 16:00",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Screening screening2 = new Screening(movie2, room1, LocalDateTime.parse("2021-03-14 16:00",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        screeningRepository.save(screening);
        screeningRepository.save(screening1);
        screeningRepository.save(screening2);


    }
}
