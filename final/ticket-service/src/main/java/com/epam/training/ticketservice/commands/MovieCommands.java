package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.dto.MovieDto;
import com.epam.training.ticketservice.model.AccountType;
import com.epam.training.ticketservice.services.AccountService;
import com.epam.training.ticketservice.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class MovieCommands {
    private final AccountService accountService;
    private final MovieService movieService;

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create movie", value = "Create movie by <title> <type> <length in minutes>")
    public String createMovie(String title, String type, int length){
        MovieDto movieDto = new MovieDto(title, type, length);
        movieService.createMovie(movieDto);
        return String.format("Successfully created movie '%s'", movieDto.getTitle());
    }

    @ShellMethod(key = "update movie", value = "Update movie by <title> <type> <length in minutes>")
    public String updateMovie(String title, String type, int length){
        movieService.updateMovie(title, type, length);
        return String.format("Successfully updated movie '%s'", title);
    }

    @ShellMethod(key = "delete movie", value = "Delete movie by <title>")
    public String deleteMovie(String title){
        movieService.deleteMovie(title);
        return String.format("Successfully deleted movie '%s'", title);
    }

    @ShellMethod(key = "list movies", value = "List movies")
    public List<MovieDto> listMovies(){
        return movieService.getMovieList();
    }

    private Availability isAvailable() {
        Optional<AccountDto> user = accountService.describe();
        return user.isPresent() && user.get().accountType() == AccountType.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }
}
