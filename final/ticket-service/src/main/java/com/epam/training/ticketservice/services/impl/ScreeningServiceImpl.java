package com.epam.training.ticketservice.services.impl;

import com.epam.training.ticketservice.dto.ScreeningDto;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.exceptions.ScreeningOverlappingException;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.repositories.MovieRepository;
import com.epam.training.ticketservice.repositories.RoomRepository;
import com.epam.training.ticketservice.repositories.ScreeningRepository;
import com.epam.training.ticketservice.services.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {
    private static final String SCREENING_NOT_FOUND = "Screening not found.";
    private static final String SCREENING_STARTS_IN_BREAK = "This would start in the break period after another screening in this room";
    private static final String SCREENINGS_OVERLAPPING = "There is an overlapping screening";

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void createScreening(String title, String name, LocalDateTime dateTime) throws NotFoundException, ScreeningOverlappingException {
        var movie = movieRepository.findByTitle(title);
        if (movie.isEmpty())
            throw new NotFoundException(SCREENING_NOT_FOUND);
        var room = roomRepository.findByName(name);
        if (room.isEmpty())
            throw new NotFoundException(SCREENING_NOT_FOUND);
        Screening screening = new Screening(
                movie.get(),
                room.get(),
                dateTime
        );
        overlapValidate(screening);
        screeningRepository.save(screening);
    }

    @Override
    public void deleteScreening(String title, String name, LocalDateTime dateTime) throws NotFoundException {
        var movie = movieRepository.findByTitle(title);
        if (movie.isEmpty())
            throw new NotFoundException(SCREENING_NOT_FOUND);
        var room = roomRepository.findByName(name);
        if (room.isEmpty())
            throw new NotFoundException(SCREENING_NOT_FOUND);
        var screening = screeningRepository.findScreeningByMovieAndRoomAndDate(
                movie.get(),
                room.get(),
                dateTime
        );
        if (screening.isEmpty())
            throw new NotFoundException(SCREENING_NOT_FOUND);

        screeningRepository.delete(screening.get());
    }

    @Override
    public List<ScreeningDto> getScreeningList() {
        return screeningRepository.findAll().stream().map(ScreeningDto::new).toList();
    }

    private void overlapValidate(Screening screening) throws ScreeningOverlappingException {
        var listScreenings = screeningRepository.findAllByRoom(screening.getRoom());
        if (listScreenings.isEmpty()) return;
        for (Screening item : listScreenings) {
            var itemStart = item.getDate();
            var itemEnd = itemStart.plusMinutes(item.getMovie().getLength());
            //System.out.println(itemStart.format(formatter) + "; " + itemEnd.format(formatter));

            var screeningStart = screening.getDate();
            var screeningEnd = screeningStart.plusMinutes(screening.getMovie().getLength());
            //System.out.println(screeningStart.format(formatter) + "; " + screeningEnd.format(formatter) + "\n");

            if (screeningStart.isAfter(itemStart) && screeningStart.isBefore(itemEnd) ||
                    screeningEnd.isAfter(itemStart) && screeningEnd.isBefore(itemEnd) ||
                    screeningStart.isAfter(itemStart) && screeningEnd.isBefore(itemEnd) ||
                    (screeningEnd.isEqual(itemEnd) || screeningStart.isEqual(itemStart)) ||
                    (screeningEnd.isEqual(itemStart)) || (screeningStart.isEqual(itemEnd))) {
                throw new ScreeningOverlappingException(SCREENINGS_OVERLAPPING);
            }
            if (screeningEnd.isBefore(itemStart) && screeningEnd.plusMinutes(10).isAfter(itemStart) ||
                    screeningStart.isAfter(itemEnd) && screeningStart.isBefore(itemEnd.plusMinutes(10))){
                throw new ScreeningOverlappingException(SCREENING_STARTS_IN_BREAK);
            }
        }
    }
}
