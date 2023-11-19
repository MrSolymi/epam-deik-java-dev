package com.epam.training.ticketservice.services.impl;

import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.dto.BookingDto;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.exceptions.SeatAlreadyTakenException;
import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.AccountType;
import com.epam.training.ticketservice.model.Booking;
import com.epam.training.ticketservice.model.Seat;
import com.epam.training.ticketservice.repositories.AccountRepository;
import com.epam.training.ticketservice.repositories.BookingRepository;
import com.epam.training.ticketservice.repositories.MovieRepository;
import com.epam.training.ticketservice.repositories.RoomRepository;
import com.epam.training.ticketservice.repositories.ScreeningRepository;
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
    private static final String MOVIE_NOT_FOUND = "The movie with the given title is not found";
    private static final String ROOM_NOT_FOUND = "The room with the given name is not found";
    private static final String SCREENING_NOT_FOUND =
            "The screening with the given data is not found";
    private static final String ACCOUNT_NOT_FOUND = "The account is not found";

    private final BookingRepository bookingRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ScreeningRepository screeningRepository;
    private final AccountRepository accountRepository;

    public List<BookingDto> getBookingList(AccountDto accountDto) throws NotFoundException {
        Optional<Account> account = accountRepository.findByUsername(accountDto.username());
        if (account.isEmpty()) {
            throw new NotFoundException(ACCOUNT_NOT_FOUND);
        }
        return bookingRepository.findAllByAccount(account.get()).stream().map(BookingDto::new).toList();
    }

    @Override
    public void createBooking(String movieTitle,
                              String roomName,
                              LocalDateTime startTime,
                              String seatListString,
                              AccountDto accountDto) throws NotFoundException, SeatAlreadyTakenException {
        var movie = movieRepository.findByTitle(movieTitle);
        if (movie.isEmpty()) {
            throw new NotFoundException(MOVIE_NOT_FOUND);
        }

        var room = roomRepository.findByName(roomName);
        if (room.isEmpty()) {
            throw new NotFoundException(ROOM_NOT_FOUND);
        }

        var screening = screeningRepository.findScreeningByMovieAndRoomAndDate(
                movie.get(),
                room.get(),
                startTime
        );
        if (screening.isEmpty()) {
            throw new NotFoundException(SCREENING_NOT_FOUND);
        }

        List<Seat> seatList = new ArrayList<>();
        List<Seat> seatListInBooking = new ArrayList<>();
        String[] seatsInString = seatListString.split(" ");
        for (String item : seatsInString) {
            String[] s = item.split(",");
            if (room.get().getNumberOfRows() < Integer.parseInt(s[0])
                    || room.get().getNumberOfColumns() < Integer.parseInt(s[1])) {
                String seatNotFound =
                        "Seat ("
                        + Integer.parseInt(s[0])
                        + "," + Integer.parseInt(s[1])
                        + ") does not exist in this room";
                throw new NotFoundException(seatNotFound);
            }

            seatList.add(new Seat(Integer.parseInt(s[0]), Integer.parseInt(s[1])));
        }

        List<Booking> bookingList = bookingRepository.findAllByScreening(screening.get()).stream().toList();

        for (Booking itemForSeats : bookingList) {
            List<Seat> seatsInRepo = itemForSeats.getSeats();
            seatListInBooking.addAll(seatsInRepo);
        }

        for (Seat seat : seatList) {
            if (seatListInBooking.contains(seat)) {
                throw new SeatAlreadyTakenException("Seat ("
                        + seat.getRowIndex() + "," + seat.getColumnIndex()
                        + ") is already taken");
            }
        }

        Optional<Account> account = accountRepository.findByUsername(accountDto.username());
        if (account.isEmpty()) {
            throw new NotFoundException(ACCOUNT_NOT_FOUND);
        }

        //?????????
        if (account.get().getAccountType().equals(AccountType.ADMIN)) {
            return;
        }
        Booking booking = new Booking(
                account.get(),
                screening.get(),
                seatList
        );
        bookingRepository.save(booking);
    }
}
