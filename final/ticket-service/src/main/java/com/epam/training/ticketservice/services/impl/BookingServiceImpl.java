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
import java.util.Arrays;
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

    public Booking getBookingForGetPrice(String movieTitle,
                                         String roomName,
                                         LocalDateTime startDate,
                                         String seatListString) throws NotFoundException {
        var movie = movieRepository.findByTitle(movieTitle);
        if (movie.isEmpty()) {
            throw new NotFoundException(MOVIE_NOT_FOUND);
        }

        var room = roomRepository.findByName(roomName);
        if (room.isEmpty()) {
            throw new NotFoundException(ROOM_NOT_FOUND);
        }

        var screening = screeningRepository.findScreeningByMovieAndRoomAndDate(movie.get(), room.get(), startDate);
        if (screening.isEmpty()) {
            throw new NotFoundException(SCREENING_NOT_FOUND);
        }

        List<Seat> seatsToBook = new ArrayList<>();

        Arrays.stream(seatListString.split(" "))
                .forEach(x -> {
                    List<String> seatSpot = Arrays.asList(x.split(","));
                    seatsToBook.add(new Seat(Integer.parseInt(seatSpot.get(0)), Integer.parseInt(seatSpot.get(1))));
                });

        return new Booking(screening.get(), seatsToBook, calculator.calculate(screening.get(), seatsToBook.size()));
    }

    @Override
    public int getPrice(String movieTitle, String roomName, LocalDateTime startDate, String seatListString)
            throws NotFoundException {

        Booking booking = getBookingForGetPrice(movieTitle, roomName, startDate, seatListString);
        if (!isBookingValid(booking) || booking.getScreening() == null) {
            throw new NotFoundException("Not a valid booking");
        }
        return booking.getPrice();
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

        if (isBookingValid(booking)) {
            bookingRepository.save(booking);
        }

    }
    /*
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

     */
    private boolean isBookingValid(Booking booking) throws NotFoundException {

        List<Booking> bookingsAtPlace = bookingRepository.findAllByScreening_MovieAndScreening_RoomAndScreening_Date(
                booking.getScreening().getMovie(),
                booking.getScreening().getRoom(),
                booking.getScreening().getDate());

        return isSeatPresent(booking) && isSeatNotBooked(booking, bookingsAtPlace);
    }

    private boolean isSeatPresent(Booking booking) throws NotFoundException {

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
            //sb.delete(sb.length(), sb.length() - 2).append(" does not exist in this room");
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
            //sb.delete(sb.length(), sb.length() - 2).append(" is already taken");
            sb.append(" is already taken");
            String seatNotFound = sb.toString();
            throw new NotFoundException(seatNotFound);
        }
    }
}
