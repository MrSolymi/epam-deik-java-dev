package com.epam.training.ticketservice.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class MovieCommands {
    @ShellMethod(key = "create movie", value = "Create movie by \"<title> <type> <length in minutes>\"")
    public String createMovie(String title, String type, int length){
        return String.format("Successfully created movie '%s'", title);
    }

    @ShellMethod(key = "update movie", value = "Update movie by \"<title> <type> <length in minutes>\"")
    public String updateMovie(String title, String type, int length){
        return String.format("Successfully updated movie '%s'", title);
    }

    @ShellMethod(key = "delete movie", value = "Delete movie by \"<title>\"")
    public String deleteMovie(String title, String type, int length){
        return String.format("Successfully deleted movie '%s'", title);
    }

    @ShellMethod(key = "list movies", value = "list movies")
    public String listMovies(){
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }
}
