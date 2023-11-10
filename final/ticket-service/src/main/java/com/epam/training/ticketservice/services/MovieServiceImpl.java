package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dto.MovieDto;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    @Override
    public void updateMovie(String title, String type, int length) {
        Optional<Movie> movie = movieRepository.findByTitle(title);
        if (movie.isEmpty())
            return;
        movie.get().setType(type);
        movie.get().setLength(length);
        movieRepository.save(movie.get());
    }

    @Override
    public void deleteMovie(String title) {
        Optional<Movie> movie = movieRepository.deleteByTitle(title);
        if (movie.isEmpty())
            return;
        movieRepository.deleteByTitle(title);
    }

    @Override
    public void createMovie(MovieDto movieDto) {
        Movie movie = new Movie(
                movieDto.getTitle(),
                movieDto.getType(),
                movieDto.getLength()
        );
        movieRepository.save(movie);
    }

    @Override
    public List<MovieDto> getMovieList() {
        return movieRepository.findAll().stream().map(MovieDto::new).toList();
    }
}
