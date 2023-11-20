package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.services.AccountService;
import com.epam.training.ticketservice.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class BookingCommands {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final BookingService bookingService;
    private final AccountService accountService;

    @ShellMethod(key = "book",
            value = "Book by <movie title> <room name> <starting date in YYYY-MM-DD hh:mm format>"
                    + " seat in \"<row>,<column>\" format, for book more seat separate the seats with space key")
    public String createBooking(String movieTitle, String roomName, String startTime, String seatListString)
            throws NotFoundException {
        Optional<AccountDto> accountDto = accountService.describe();
        if (accountDto.isEmpty()) {
            return "You need to sign in first";
        }
        try {
            bookingService.createBooking(
                    movieTitle,
                    roomName,
                    LocalDateTime.parse(startTime, formatter),
                    seatListString, accountDto.get());
        } catch (Exception e) {
            return e.getMessage();
        }
        return bookingService.getBookingList(accountDto.get())
                .get(bookingService.getBookingList(accountDto.get()).size() - 1).toStringBooked();
    }
}
