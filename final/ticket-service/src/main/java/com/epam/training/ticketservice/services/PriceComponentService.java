package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.model.Screening;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface PriceComponentService {

    void createPriceComponent(String componentName, int componentValue) throws AlreadyExistsException;

    int getPrice(String movieTitle, String roomName, LocalDateTime dateTime, String seatListInString)
            throws NotFoundException;

    int getPrice(Screening screening);

    void priceComponentToRoom(String componentName, String roomName) throws NotFoundException;

    void priceComponentToMovie(String componentName, String roomName) throws NotFoundException;

    void priceComponentToScreening(String componentName,
                                   String movieTitle,
                                   String roomName,
                                   LocalDateTime dateTime) throws NotFoundException;
}
