package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dto.ScreeningDto;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.exceptions.ScreeningOverlappingException;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningService {
    void createScreening(String movieTitle, String roomName, LocalDateTime startTime)
            throws ScreeningOverlappingException, NotFoundException;

    List<ScreeningDto> getScreeningList();

    void deleteScreening(String movieTitle, String roomName, LocalDateTime startTime) throws NotFoundException;
}
