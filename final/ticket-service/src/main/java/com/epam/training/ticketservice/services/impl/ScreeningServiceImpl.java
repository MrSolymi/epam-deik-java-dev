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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {
    private static final String SCREENING_NOT_FOUND =
            "The screening with the given data is not found";
    private static final String SCREENING_STARTS_IN_BREAK =
            "This would start in the break period after another screening in this room";
    private static final String SCREENINGS_OVERLAPPING =
            "There is an overlapping screening";
    private static final String MOVIE_NOT_FOUND = "The movie with the given title is not found";
    private static final String ROOM_NOT_FOUND = "The room with the given name is not found";

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    @Override
    public void createScreening(String title,
                                String name,
                                LocalDateTime dateTime) throws ScreeningOverlappingException, NotFoundException {
        var movie = movieRepository.findByTitle(title);
        if (movie.isEmpty()) {
            throw new NotFoundException(MOVIE_NOT_FOUND);
        }
        var room = roomRepository.findByName(name);
        if (room.isEmpty()) {
            throw new NotFoundException(ROOM_NOT_FOUND);
        }
        Screening screening = new Screening(
                movie.get(),
                room.get(),
                dateTime
        );
        overlapValidate(screening);
        screeningRepository.save(screening);
    }

    @Override
    public List<ScreeningDto> getScreeningList() {
        return screeningRepository.findAll().stream().map(ScreeningDto::new).toList();
    }

    @Override
    public void deleteScreening(String movieTitle, String roomName, LocalDateTime startTime) throws NotFoundException {
        var movie = movieRepository.findByTitle(movieTitle);
        if (movie.isEmpty()) {
            throw new NotFoundException(MOVIE_NOT_FOUND);
        }

        var room = roomRepository.findByName(roomName);
        if (room.isEmpty()) {
            throw new NotFoundException(ROOM_NOT_FOUND);
        }
        Optional<Screening> screening =
                screeningRepository.findScreeningByMovieAndRoomAndDate(
                        movie.get(),
                        room.get(),
                        startTime
                );
        if (screening.isEmpty()) {
            throw new NotFoundException(SCREENING_NOT_FOUND);
        }
        screeningRepository.delete(screening.get());
    }

    private void overlapValidate(Screening screening) throws ScreeningOverlappingException {
        var listScreenings = screeningRepository.findAllByRoom(screening.getRoom());
        if (listScreenings.isEmpty()) {
            return;
        }
        for (Screening item : listScreenings) {
            var itemStart = item.getDate();
            var itemEnd = itemStart.plusMinutes(item.getMovie().getLength());
            //System.out.println(itemStart.format(formatter) + "; " + itemEnd.format(formatter));

            var screeningStart = screening.getDate();
            var screeningEnd = screeningStart.plusMinutes(screening.getMovie().getLength());
            //System.out.println(screeningStart.format(formatter) + "; " + screeningEnd.format(formatter) + "\n");
            if (screeningStart.isAfter(itemStart) && screeningStart.isBefore(itemEnd)
                    || screeningEnd.isAfter(itemStart) && screeningEnd.isBefore(itemEnd)
                    || screeningStart.isAfter(itemStart) && screeningEnd.isBefore(itemEnd)
                    || (screeningEnd.isEqual(itemEnd) || screeningStart.isEqual(itemStart))
                    || (screeningEnd.isEqual(itemStart)) || (screeningStart.isEqual(itemEnd))) {
                throw new ScreeningOverlappingException(SCREENINGS_OVERLAPPING);
            }
            if (screeningEnd.isBefore(itemStart) && screeningEnd.plusMinutes(10).isAfter(itemStart)
                    || screeningStart.isAfter(itemEnd) && screeningStart.isBefore(itemEnd.plusMinutes(10))) {
                throw new ScreeningOverlappingException(SCREENING_STARTS_IN_BREAK);
            }
        }
    }
}
