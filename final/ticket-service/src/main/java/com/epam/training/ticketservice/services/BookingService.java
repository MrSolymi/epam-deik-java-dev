package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.dto.BookingDto;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    void createBooking(String movieTitle, String roomName, LocalDateTime startTime, String seatListString, AccountDto accountDto);
    //boolean hasBookings(AccountDto accountDto);
    List<BookingDto> getBookingsList();
}
