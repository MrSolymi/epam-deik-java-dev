package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dto.MovieDto;
import com.epam.training.ticketservice.dto.RoomDto;
import com.epam.training.ticketservice.dto.ScreeningDto;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.repositories.MovieRepository;
import com.epam.training.ticketservice.repositories.RoomRepository;
import com.epam.training.ticketservice.repositories.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService{

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    /*
    @Override
    public void createScreening(ScreeningDto screeningDto) {
        Screening screening = new Screening(
                movieRepository.findByTitle(screeningDto.getMovieDto().getTitle()).get(),
                roomRepository.findByName(screeningDto.getRoomDto().getName()).get(),
                screeningDto.getDate()
        );
        screeningRepository.save(screening);
    } */

    @Override
    public void createScreening(MovieDto movieDto, RoomDto roomDto, LocalDateTime dateTime) {
        Screening screening = new Screening(
                movieRepository.findByTitle(movieDto.getTitle()).get(),
                roomRepository.findByName(roomDto.getName()).get(),
                dateTime
        );
        screeningRepository.save(screening);
    }

    @Override
    public List<ScreeningDto> getScreeningList() {
        return screeningRepository.findAll().stream().map(ScreeningDto::new).toList();
    }
}
