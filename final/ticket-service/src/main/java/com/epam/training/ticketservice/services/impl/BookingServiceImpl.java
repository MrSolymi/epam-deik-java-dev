package com.epam.training.ticketservice.services.impl;

import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.dto.BookingDto;
import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.AccountType;
import com.epam.training.ticketservice.model.Booking;
import com.epam.training.ticketservice.model.Seat;
import com.epam.training.ticketservice.repositories.*;
import com.epam.training.ticketservice.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ScreeningRepository screeningRepository;
    private final AccountRepository accountRepository;


    @Override
    public void createBooking(String movieTitle, String roomName, LocalDateTime startTime, String seatListString, AccountDto accountDto) {
        var movie = movieRepository.findByTitle(movieTitle);
        if (movie.isEmpty()) return;

        var room = roomRepository.findByName(roomName);
        if (room.isEmpty()) return;

        var screening = screeningRepository.findScreeningByMovieAndRoomAndDate(
                movie.get(),
                room.get(),
                startTime
        );
        if (screening.isEmpty()) return;

        List<Seat> seatList = new ArrayList<>();
        String[] seatsInString = seatListString.split(" ");
        for (String item : seatsInString) {
            String[] s = item.split(",");
            seatList.add(new Seat(Integer.parseInt(s[0]), Integer.parseInt(s[1])));
        }

        Optional<Account> account = accountRepository.findByUsername(accountDto.username());
        if (account.isEmpty()) return;

        if (account.get().getAccountType().equals(AccountType.ADMIN)) return;
        Booking booking = new Booking(
                account.get(),
                screening.get(),
                seatList
        );
        bookingRepository.save(booking);
    }

    /*
    @Override
    public boolean hasBookings(AccountDto accountDto) {
        Optional<Account> account = accountRepository.findByUsername(accountDto.username());
        if (account.isEmpty()) return false;
        List<Booking>  bookingList = bookingRepository.findAllByAccount(account.get());
        return !bookingList.isEmpty();
    }

     */

    @Override
    public List<BookingDto> getBookingsList() {
        return bookingRepository.findAll().stream().map(BookingDto::new).toList();
    }
}
