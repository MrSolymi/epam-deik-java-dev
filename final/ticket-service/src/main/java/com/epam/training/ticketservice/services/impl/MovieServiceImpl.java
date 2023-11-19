package com.epam.training.ticketservice.services.impl;

import com.epam.training.ticketservice.dto.MovieDto;
import com.epam.training.ticketservice.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.repositories.MovieRepository;
import com.epam.training.ticketservice.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private static final String MOVIE_AlREADY_EXIST = "The movie with the given title already exist";
    private static final String MOVIE_NOT_FOUND = "The movie with the given title not found";

    private final MovieRepository movieRepository;

    @Override
    public void createMovie(String title, String type, int length) throws AlreadyExistsException {
        if (movieRepository.findByTitle(title).isPresent()) {
            throw new AlreadyExistsException(MOVIE_AlREADY_EXIST);
        }
        Movie movie = new Movie(title, type, length);
        movieRepository.save(movie);
    }

    @Override
    public void updateMovie(String title, String type, int length) throws NotFoundException {
        Optional<Movie> movie = movieRepository.findByTitle(title);
        if (movie.isEmpty()) {
            throw new NotFoundException(MOVIE_NOT_FOUND);
        }
        movie.get().setType(type);
        movie.get().setLength(length);
        movieRepository.save(movie.get());
    }

    @Override
    public void deleteMovie(String title) throws NotFoundException {
        Optional<Movie> movie = movieRepository.findByTitle(title);
        if (movie.isEmpty()) {
            throw new NotFoundException(MOVIE_NOT_FOUND);
        }
        movieRepository.deleteByTitle(title);
    }

    @Override
    public List<MovieDto> getMovieList() {
        return movieRepository.findAll().stream().map(MovieDto::new).toList();
    }
}
