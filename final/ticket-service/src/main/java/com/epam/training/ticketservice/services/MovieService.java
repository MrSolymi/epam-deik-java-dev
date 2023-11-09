package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dto.MovieDto;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<MovieDto> getMovieList();
    void updateMovie(String title, String type, int length);
    void deleteMovie(String title);
    void createMovie(MovieDto movieDto);
}
