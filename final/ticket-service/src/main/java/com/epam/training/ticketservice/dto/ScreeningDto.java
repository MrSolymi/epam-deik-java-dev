package com.epam.training.ticketservice.dto;

import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@RequiredArgsConstructor
public class ScreeningDto {

    private final MovieDto movieDto;
    private final RoomDto roomDto;
    private final LocalDateTime date;

    public ScreeningDto(Screening screening) {
        movieDto = new MovieDto(screening.getMovie());
        roomDto = new RoomDto(screening.getRoom());
        date = screening.getDate();
    }


    @Override
    public String toString() {
        return movieDto + ", screened in room " + roomDto.getName() + ", at " + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
