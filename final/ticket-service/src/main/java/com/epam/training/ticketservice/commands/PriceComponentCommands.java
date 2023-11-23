package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.components.Calculator;
import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.model.AccountType;
import com.epam.training.ticketservice.services.AccountService;
import com.epam.training.ticketservice.services.BookingService;
import com.epam.training.ticketservice.services.PriceComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class PriceComponentCommands {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final AccountService accountService;
    private final PriceComponentService priceComponentService;
    private final BookingService bookingService;

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update base price", value = "update base price <new price>")
    public void updateBasePrice(int newBasePrice) {
        Calculator.setBasePrice(newBasePrice);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create price component",
            value = "create price component <name of component> <value of component>")
    public String createPriceComponent(String componentName, int componentValue) {
        try {
            priceComponentService.createPriceComponent(componentName, componentValue);
        } catch (Exception e) {
            return e.getMessage();
        }

        return "Successfully created " + componentName + " price component";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "attach price component to room",
            value = "attach price component to room <name of component> <name of room>")
    public String attachComponentToRoom(String componentName, String roomName) {
        try {
            priceComponentService.priceComponentToRoom(componentName, roomName);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Successfully attached the " + componentName + " component to " + roomName + " room";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "attach price component to movie",
            value = "attach price component to movie <name of component> <title of movie>")
    public String attachComponentToMovie(String componentName, String movieTitle) {
        try {
            priceComponentService.priceComponentToMovie(componentName, movieTitle);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Successfully attached the " + componentName + " component to " + movieTitle + " movie";
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
        try {
            priceComponentService.priceComponentToScreening(componentName, movieTitle, roomName,
                    LocalDateTime.parse(startDate, formatter));
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Successfully attached the " + componentName + " component to screening";
    }

    @ShellMethod(group = "Booking Commands", key = "show price for",
            value = "show price for <title of movie> "
                    + "<name of room> <screening staring time in YYYY-MM-DD hh:mm format> "
                    + "<list of seats separated by space key, seats in \"<row>,<column>\" format")
    public String showPrice(String movieTitle, String roomName, String startDate, String seatListInString) {
        int price = 0;
        try {
            price = bookingService.showPrice(movieTitle, roomName, LocalDateTime.parse(startDate, formatter),
                    seatListInString);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "The price for this booking would be " + price + " HUF";
    }

    private Availability isAvailable() {
        Optional<AccountDto> user = accountService.describe();
        return user.isPresent() && user.get().accountType() == AccountType.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }
}
