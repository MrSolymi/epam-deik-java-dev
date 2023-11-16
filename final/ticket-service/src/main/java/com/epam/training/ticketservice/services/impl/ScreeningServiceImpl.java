package com.epam.training.ticketservice.services.impl;

import com.epam.training.ticketservice.dto.ScreeningDto;
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

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    private boolean isDateAvailable(LocalDateTime startDate,
                                    LocalDateTime endDate,
                                    LocalDateTime startDateToCheck,
                                    LocalDateTime endDateToCheck,
                                    int breakTime) {

        return !((startDateToCheck.isBefore(endDate.plusMinutes(breakTime)) && startDateToCheck.isAfter(startDate))
                || (endDateToCheck.isBefore(endDate) && endDateToCheck.isAfter(startDate)))
                && !(startDate.equals(startDateToCheck) || endDate.equals(endDateToCheck));
    }
    private boolean isValidScreening(Screening screening, int breakTime) {

        List<Screening> screenings = screeningRepository.findAllByRoom_Name(screening.getRoom().getName());
        if (screenings.isEmpty())
            return true;
        for (Screening item : screenings) {
            if (!isDateAvailable(
                    item.getDate(),
                    item.getDate().plusMinutes(item.getMovie().getLength()),
                    screening.getDate(),
                    screening.getDate().plusMinutes(screening.getMovie().getLength()), breakTime)
            ) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void createScreening(String movieTitle, String roomName, LocalDateTime startTime) {
        var movie = movieRepository.findByTitle(movieTitle);
        if (movie.isEmpty()) return;

        var room = roomRepository.findByName(roomName);
        if (room.isEmpty()) return;
        Screening screening = new Screening(movie.get(), room.get(), startTime);

        if (isValidScreening(screening, 0)){
            if (isValidScreening(screening, 10)){
                screeningRepository.save(screening);
                System.out.println("Successfully created screening");
            }
            else {
                System.out.println("This would start in the break period after another screening in this room");
            }
            return;
        }
        System.out.println("There is an overlapping screening");
    }

    @Override
    public List<ScreeningDto> getScreeningList() {
        return screeningRepository.findAll().stream().map(ScreeningDto::new).toList();
    }

    @Override
    public void deleteScreening(String movieTitle, String roomName, LocalDateTime startTime) {
        var movie = movieRepository.findByTitle(movieTitle);
        if (movie.isEmpty()) return;

        var room = roomRepository.findByName(roomName);
        if (room.isEmpty()) return;
        Optional<Screening> screening =
                screeningRepository.findScreeningByMovieAndRoomAndDate(
                        movie.get(),
                        room.get(),
                        startTime
                );
        if (screening.isEmpty())
            return;
        screeningRepository.delete(screening.get());
    }
}
