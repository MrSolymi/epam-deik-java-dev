package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.model.AccountType;
import com.epam.training.ticketservice.services.AccountService;
import com.epam.training.ticketservice.services.PriceComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class PriceComponentCommands {
    private final AccountService accountService;
    private final PriceComponentService priceComponentService;

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update base price", value = "update base price <new price>")
    public String updateBasePrice(int newBasePrice) {

        return "";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create price component",
            value = "create price component <name of component> <value of component>")
    public String createPriceComponent(String componentName, int componentValue) {
        try {
            priceComponentService.createPriceComponent(componentName, componentValue);
        } catch (AlreadyExistsException e) {
            return e.getMessage();
        }
        return String.format("Successfully created '%s' price component", componentName);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "attach price component to room",
            value = "attach price component to room <name of component> <name of room>")
    public String attachComponentToRoom(String componentName, String roomName) {

        return "";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "attach price component to movie",
            value = "attach price component to movie <name of component> <title of movie>")
    public String attachComponentToMovie(String componentName, String movieName) {

        return "";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(
            key = "attach price component to screening",
            value = "attach price component to screening <name of component> "
                    + "<title of movie> <name of movie> <screening staring time in YYYY-MM-DD hh:mm format>")
    public String attachComponentToScreening(String componentName,
                                             String movieTitle,
                                             String roomName,
                                             String startDate) {

        return "";
    }

    @ShellMethod(
            key = "show price for",
            value = "show price for <title of movie> "
                    + "<name of room> <screening staring time in YYYY-MM-DD hh:mm format> "
                    + "<list of seats separated by space key, seats in \"<row>,<column>\" format")
    public String getPrice(String movieTitle, String roomName, String startDate, String seatListString) {

        return "";
    }

    private Availability isAvailable() {
        Optional<AccountDto> user = accountService.describe();
        return user.isPresent() && user.get().accountType() == AccountType.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }
}
