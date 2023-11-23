package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.dto.BookingDto;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.exceptions.SeatAlreadyTakenException;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    void createBooking(String movieTitle,
                       String roomName,
                       LocalDateTime startTime,
                       String seatListString,
                       AccountDto accountDto
    ) throws NotFoundException, SeatAlreadyTakenException;

    List<BookingDto> getBookingList(AccountDto accountDto) throws NotFoundException;

    int showPrice(String movieTitle, String roomName, LocalDateTime dateTime, String seatListInString)
            throws NotFoundException;

}
