package com.epam.training.ticketservice.dto;

import com.epam.training.ticketservice.model.Movie;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MovieDto {
    private final String title;
    private final String type;
    private final int length;

    public MovieDto(Movie movie) {
        title = movie.getTitle();
        type = movie.getType();
        length = movie.getLength();
    }

    @Override
    public String toString() {
        return title + " (" + type + ", " + length + " minutes)";
    }

}
