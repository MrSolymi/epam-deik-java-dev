package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dto.MovieDto;
import com.epam.training.ticketservice.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.exceptions.NotFoundException;

import java.util.List;

public interface MovieService {
    List<MovieDto> getMovieList();
    void updateMovie(String title, String type, int length) throws NotFoundException;
    void deleteMovie(String title) throws NotFoundException;
    void createMovie(String title, String type, int length) throws AlreadyExistsException;
}
