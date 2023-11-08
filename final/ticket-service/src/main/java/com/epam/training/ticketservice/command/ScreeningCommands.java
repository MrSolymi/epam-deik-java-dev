package com.epam.training.ticketservice.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ScreeningCommands {
    @ShellMethod(key = "create screening", value = "Create screening by <movie title> <room name> <starting date in YYYY-MM-DD hh:mm format>")
    public String createScreening(String movieTitle, String roomName, String startDate){
        return "Successfully created screening";
    }

    @ShellMethod(key = "delete screening", value = "Delete screening by <movie title> <room name> <starting date in YYYY-MM-DD hh:mm format>")
    public String deleteScreening(String movieTitle, String roomName, String startDate){
        return "Successfully created screening";
    }

    @ShellMethod(key = "list screenings", value = "List screenings")
    public String listScreenings(String movieTitle, String roomName, String startDate){
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }
}
