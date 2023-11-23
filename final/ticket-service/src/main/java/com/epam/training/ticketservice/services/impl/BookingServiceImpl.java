package com.epam.training.ticketservice.services.impl;

import com.epam.training.ticketservice.components.Calculator;
import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.dto.BookingDto;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.exceptions.SeatAlreadyTakenException;
import com.epam.training.ticketservice.model.Account;
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

    private final Calculator calculator;

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
        var screening = screeningRepository.findScreeningByMovieAndRoomAndDate(movie.get(), room.get(), startTime);
        if (screening.isEmpty()) {
            throw new NotFoundException(SCREENING_NOT_FOUND);
        }
        Optional<Account> account = accountRepository.findByUsername(accountDto.username());
        if (account.isEmpty()) {
            throw new NotFoundException(ACCOUNT_NOT_FOUND);
        }

        List<Seat> seatList = new ArrayList<>();
        String[] seatsInString = seatListString.split(" ");
        for (String item : seatsInString) {
            String[] s = item.split(",");
            seatList.add(new Seat(Integer.parseInt(s[0]), Integer.parseInt(s[1])));
        }

        Booking booking = new Booking(
                account.get(),
                screening.get(),
                seatList
        );
        booking.setPrice(calculator.calculate(screening.get(), seatList.size()));

        if (canBooking(booking)) {
            bookingRepository.save(booking);
        }

    }

    public boolean canBooking(Booking booking) throws NotFoundException {

        List<Booking> bookingsAtPlace = bookingRepository.findAllByScreening_MovieAndScreening_RoomAndScreening_Date(
                booking.getScreening().getMovie(),
                booking.getScreening().getRoom(),
                booking.getScreening().getDate());

        return isSeatExists(booking) && isSeatNotBooked(booking, bookingsAtPlace);
    }

    private boolean isSeatExists(Booking booking) throws NotFoundException {

        List<Seat> notExistentSeatsInRoom = booking.getSeats()
                .stream().filter(x -> x.getRowIndex() > booking.getScreening()
                        .getRoom()
                        .getNumberOfColumns()
                        || x.getColumnIndex() > booking.getScreening()
                        .getRoom()
                        .getNumberOfRows())
                .toList();

        List<String> seatList = new ArrayList<>();

        notExistentSeatsInRoom.forEach(x ->
                seatList.add(String.format("(%d,%d)", x.getRowIndex(), x.getColumnIndex())));

        if (notExistentSeatsInRoom.isEmpty()) {
            return true;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Seat ");
            for (String s : seatList) {
                sb.append(s);
            }
            sb.append(" does not exist in this room");
            String seatNotFound = sb.toString();
            throw new NotFoundException(seatNotFound);
        }
    }

    private boolean isSeatNotBooked(Booking booking, List<Booking> bookingsAtPlace) throws NotFoundException {

        List<Seat> bookedSeats = booking.getSeats()
                .stream()
                .filter(x -> bookingsAtPlace.stream()
                        .map(Booking::getSeats)
                        .anyMatch(y -> y.contains(x)))
                .toList();

        List<String> seatList = new ArrayList<>();

        bookedSeats.forEach(x ->
                seatList.add(String.format("(%d,%d)", x.getRowIndex(), x.getColumnIndex())));

        if (bookedSeats.isEmpty()) {
            return true;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Seat ");
            for (String s : seatList) {
                sb.append(s);
            }
            sb.append(" is already taken");
            String seatNotFound = sb.toString();
            throw new NotFoundException(seatNotFound);
        }
    }
}
