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

        Movie movie = new Movie("Helo", "action", 60);
        movieRepository.save(movie);

        Room room = new Room("Room1", 10, 10);
        roomRepository.save(room);


        Screening screening = new Screening(movie, room,  LocalDateTime.parse("2000-12-20 10:00", formatter));
        screeningRepository.save(screening);
        screening = new Screening(movie, room,  LocalDateTime.parse("2002-12-20 10:00", formatter));
        screeningRepository.save(screening);
        screening = new Screening(movie, room,  LocalDateTime.parse("2004-12-20 10:00", formatter));
        screeningRepository.save(screening);

    }
}
