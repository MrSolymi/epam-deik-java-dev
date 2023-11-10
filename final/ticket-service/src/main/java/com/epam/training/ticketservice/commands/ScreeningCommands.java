package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.model.AccountType;
import com.epam.training.ticketservice.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Optional;

@RequiredArgsConstructor
@ShellComponent
public class ScreeningCommands {
    private final AccountService accountService;

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create screening", value = "Create screening by <movie title> <room name> <starting date in YYYY-MM-DD hh:mm format>")
    public String createScreening(String movieTitle, String roomName, String startDate){
        return "Successfully created screening";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete screening", value = "Delete screening by <movie title> <room name> <starting date in YYYY-MM-DD hh:mm format>")
    public String deleteScreening(String movieTitle, String roomName, String startDate){
        return "Successfully created screening";
    }

    @ShellMethod(key = "list screenings", value = "List screenings")
    public String listScreenings(String movieTitle, String roomName, String startDate){
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

    private Availability isAvailable() {
        Optional<AccountDto> user = accountService.describe();
        return user.isPresent() && user.get().accountType() == AccountType.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }
}
