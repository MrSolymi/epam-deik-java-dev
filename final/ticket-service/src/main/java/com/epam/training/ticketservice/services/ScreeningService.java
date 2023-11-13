package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dto.ScreeningDto;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.exceptions.ScreeningOverlappingException;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningService {
    void createScreening(String title, String name, LocalDateTime dateTime) throws NotFoundException, ScreeningOverlappingException;
    void deleteScreening(String title, String name, LocalDateTime dateTime) throws NotFoundException;
    List<ScreeningDto> getScreeningList();
}
