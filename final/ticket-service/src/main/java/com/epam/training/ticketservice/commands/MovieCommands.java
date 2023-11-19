package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.model.AccountType;
import com.epam.training.ticketservice.services.AccountService;
import com.epam.training.ticketservice.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class MovieCommands {

    private final AccountService accountService;
    private final MovieService movieService;


    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create movie", value = "Create movie by <title> <type> <length in minutes>")
    public String createMovie(String title, String type, int length) {
        try {
            movieService.createMovie(title, type, length);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Successfully created movie " + title;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update movie", value = "Update movie by <title> <type> <length in minutes>")
    public String updateMovie(String title, String type, int length) {
        try {
            movieService.updateMovie(title, type, length);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Successfully updated movie " + title;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete movie", value = "Delete movie by <title>")
    public String deleteMovie(String title) {
        try {
            movieService.deleteMovie(title);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Successfully deleted movie " + title;
    }

    @ShellMethod(key = "list movies", value = "List movies")
    public String listMovies() {
        StringBuilder sb = new StringBuilder();
        var list = movieService.getMovieList();
        if (list.isEmpty()) {
            return "There are no movies at the moment";
        }
        for (var item : list) {
            sb.append(item).append("\n");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    private Availability isAvailable() {
        Optional<AccountDto> user = accountService.describe();
        return user.isPresent() && user.get().accountType() == AccountType.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin");
    }
}
