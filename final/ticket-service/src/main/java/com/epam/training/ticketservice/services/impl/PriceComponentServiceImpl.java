package com.epam.training.ticketservice.services.impl;

import com.epam.training.ticketservice.components.Calculator;
import com.epam.training.ticketservice.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.model.Booking;
import com.epam.training.ticketservice.model.PriceComponent;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.model.Seat;
import com.epam.training.ticketservice.repositories.MovieRepository;
import com.epam.training.ticketservice.repositories.PriceComponentRepository;
import com.epam.training.ticketservice.repositories.RoomRepository;
import com.epam.training.ticketservice.repositories.ScreeningRepository;
import com.epam.training.ticketservice.services.BookingService;
import com.epam.training.ticketservice.services.PriceComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
        }
        component = Optional.of(new PriceComponent(componentName, componentValue));
        priceComponentRepository.save(component.get());
    }

    @Transactional
    @Override
    public int calculateAdditionalPrices(Screening screening) {
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

    @Transactional
    @Override
    public void priceComponentToRoom(String componentName, String roomName) throws NotFoundException {
        Optional<PriceComponent> component = priceComponentRepository.findByComponentName(componentName);
        if (component.isEmpty()) {
            throw new NotFoundException(PRICE_COMPONENT_NOT_FOUND);
        }
        var room = roomRepository.findByName(roomName);
        if (room.isEmpty()) {
            throw new NotFoundException(ROOM_NOT_FOUND);
        }



        room.get().setPriceComponent(component.get());
        var a = component.get().getRooms();
        a.add(room.get());
        component.get().setRooms(a);
        priceComponentRepository.save(component.get());
        roomRepository.save(room.get());
    }

    @Transactional
    @Override
    public void priceComponentToMovie(String componentName, String movieTitle) throws NotFoundException {
        Optional<PriceComponent> component = priceComponentRepository.findByComponentName(componentName);
        if (component.isEmpty()) {
            throw new NotFoundException(PRICE_COMPONENT_NOT_FOUND);
        }
        var movie = movieRepository.findByTitle(movieTitle);
        if (movie.isEmpty()) {
            throw new NotFoundException(MOVIE_NOT_FOUND);
        }

        movie.get().setPriceComponent(component.get());
        movieRepository.save(movie.get());
    }

    @Override
    public void priceComponentToScreening(String componentName,
                                          String movieTitle,
                                          String roomName,
                                          LocalDateTime dateTime) throws NotFoundException {
        Optional<PriceComponent> component = priceComponentRepository.findByComponentName(componentName);
        if (component.isEmpty()) {
            throw new NotFoundException(PRICE_COMPONENT_NOT_FOUND);
        }
        var movie = movieRepository.findByTitle(movieTitle);
        if (movie.isEmpty()) {
            throw new NotFoundException(MOVIE_NOT_FOUND);
        }
        var room = roomRepository.findByName(roomName);
        if (room.isEmpty()) {
            throw new NotFoundException(ROOM_NOT_FOUND);
        }

        var screening = screeningRepository.findScreeningByMovieAndRoomAndDate(movie.get(), room.get(), dateTime);
        if (screening.isEmpty()) {
            throw new NotFoundException(SCREENING_NOT_FOUND);
        }

        screening.get().setPriceComponent(component.get());
        screeningRepository.save(screening.get());
    }
}
