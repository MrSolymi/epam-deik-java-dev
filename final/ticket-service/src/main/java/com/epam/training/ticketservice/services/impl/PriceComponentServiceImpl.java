package com.epam.training.ticketservice.services.impl;

import com.epam.training.ticketservice.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.PriceComponent;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.repositories.MovieRepository;
import com.epam.training.ticketservice.repositories.PriceComponentRepository;
import com.epam.training.ticketservice.repositories.RoomRepository;
import com.epam.training.ticketservice.repositories.ScreeningRepository;
import com.epam.training.ticketservice.services.PriceComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceComponentServiceImpl implements PriceComponentService {

    private static final String COMPONENT_ALREADY_EXISTS = "The component with the given name is already exists";
    private static final String ROOM_NOT_FOUND = "The room with the given name is not found";
    private static final String MOVIE_NOT_FOUND = "The movie with the given title is not found";
    private static final String SCREENING_NOT_FOUND = "The screening with the given data is not found";
    private static final String PRICE_COMPONENT_NOT_FOUND =
            "The price component with the given component name is not found";

    private final PriceComponentRepository priceComponentRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;

    @Override
    public void createPriceComponent(String componentName, int componentValue) throws AlreadyExistsException {
        Optional<PriceComponent> component = priceComponentRepository.findByComponentName(componentName);
        if (component.isPresent()) {
            throw new AlreadyExistsException(COMPONENT_ALREADY_EXISTS);
        } else {
            priceComponentRepository.save(new PriceComponent(componentName, componentValue));
        }

    }

    @Override
    public void priceComponentToRoom(String componentName, String roomName) throws NotFoundException {
        Optional<PriceComponent> component = priceComponentRepository.findByComponentName(componentName);
        if (component.isEmpty()) {
            throw new NotFoundException(PRICE_COMPONENT_NOT_FOUND);
        } else {
            Optional<Room> room = roomRepository.findByName(roomName);
            if (room.isEmpty()) {
                throw new NotFoundException(ROOM_NOT_FOUND);
            }

            List<Room> rooms = component.get().getRooms();
            if (rooms == null) {
                rooms = new ArrayList<>();
            }
            rooms.add(room.get());
            component.get().setRooms(rooms);

            priceComponentRepository.save(component.get());
        }

    }

    @Override
    public void priceComponentToMovie(String componentName, String movieTitle) throws NotFoundException {
        Optional<PriceComponent> component = priceComponentRepository.findByComponentName(componentName);
        if (component.isEmpty()) {
            throw new NotFoundException(PRICE_COMPONENT_NOT_FOUND);
        } else {
            var priceComponent = component.get();
            List<Movie> movies = priceComponent.getMovies();
            if (movies == null) {
                movies = new ArrayList<>();
            }
            movies.add(movieRepository.findByTitle(movieTitle).get());

            priceComponent.setMovies(movies);

            priceComponentRepository.save(priceComponent);
        }

    }

    @Override
    public void priceComponentToScreening(String componentName,
                                          String movieTitle,
                                          String roomName,
                                          LocalDateTime startingTime) throws NotFoundException {
        Optional<PriceComponent> component = priceComponentRepository.findByComponentName(componentName);
        if (component.isEmpty()) {
            throw new NotFoundException(PRICE_COMPONENT_NOT_FOUND);
        } else {
            Optional<Movie> movie = movieRepository.findByTitle(movieTitle);
            if (movie.isEmpty()) {
                throw new NotFoundException(MOVIE_NOT_FOUND);
            }
            Optional<Room> room = roomRepository.findByName(roomName);
            if (room.isEmpty()) {
                throw new NotFoundException(ROOM_NOT_FOUND);
            }
            Optional<Screening> screening =
                    screeningRepository.findScreeningByMovieAndRoomAndDate(movie.get(), room.get(), startingTime);
            if (screening.isEmpty()) {
                throw new NotFoundException(SCREENING_NOT_FOUND);
            }

            List<Screening> screenings = component.get().getScreenings();
            if (screenings == null) {
                screenings = new ArrayList<>();
            }
            screenings.add(screening.get());
            component.get().setScreenings(screenings);

            priceComponentRepository.save(component.get());
        }

    }

    @Override
    public int getPriceForCalculator(Screening screening) {
        int price = 0;

        List<PriceComponent> components = priceComponentRepository.findAll();

        Optional<PriceComponent> screeningComponent =
                components.stream()
                .filter(x -> x.getScreenings().contains(screening))
                .findFirst();

        Optional<PriceComponent> roomComponent =
                components.stream()
                .filter(x -> x.getRooms().contains(screening.getRoom()))
                .findFirst();

        Optional<PriceComponent> movieComponent =
                components.stream()
                .filter(x -> x.getMovies().contains(screening.getMovie()))
                .findFirst();

        if (roomComponent.isPresent()) {
            price += roomComponent.get().getComponentValue();
        }
        if (movieComponent.isPresent()) {
            price += movieComponent.get().getComponentValue();
        }
        if (screeningComponent.isPresent()) {
            price += screeningComponent.get().getComponentValue();
        }

        return price;
    }
}
