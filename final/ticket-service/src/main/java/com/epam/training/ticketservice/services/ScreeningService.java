package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dto.ScreeningDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningService {
    void createScreening(String movieTitle, String roomName, LocalDateTime startTime);
    List<ScreeningDto> getScreeningList();

    void deleteScreening(String movieTitle, String roomName, LocalDateTime startTime);
}
