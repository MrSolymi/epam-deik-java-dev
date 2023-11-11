package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dto.MovieDto;
import com.epam.training.ticketservice.dto.RoomDto;
import com.epam.training.ticketservice.dto.ScreeningDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningService {

    void createScreening(MovieDto movieDto, RoomDto roomDto, LocalDateTime dateTime);


    List<ScreeningDto> getScreeningList();
}
