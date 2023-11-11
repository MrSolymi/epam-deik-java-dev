package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.dto.MovieDto;
import com.epam.training.ticketservice.dto.RoomDto;
import com.epam.training.ticketservice.dto.ScreeningDto;
import com.epam.training.ticketservice.model.AccountType;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.services.AccountService;
import com.epam.training.ticketservice.services.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RequiredArgsConstructor
@ShellComponent
public class ScreeningCommands {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final AccountService accountService;
    private final ScreeningService screeningService;

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create screening", value = "Create screening by <movie title> <room name> <starting date in YYYY-MM-DD hh:mm format>")
    public String createScreening(String movieTitle, String roomName, String startDate){
        MovieDto movieDto = new MovieDto(movieTitle, "", 0);
        RoomDto roomDto = new RoomDto(roomName, 0, 0);
        screeningService.createScreening(movieDto, roomDto,LocalDateTime.parse(startDate, formatter));
        return "Successfully created screening";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete screening", value = "Delete screening by <movie title> <room name> <starting date in YYYY-MM-DD hh:mm format>")
    public String deleteScreening(String movieTitle, String roomName, String startDate){
        return "Successfully created screening";
    }

    @ShellMethod(key = "list screenings", value = "List screenings")
    public String getScreeningList(){
        StringBuilder sb = new StringBuilder();
        var list = screeningService.getScreeningList();
        if (list.isEmpty())
            return "There are no screenings";
        for (var item : list){
            sb.append(item).append("\n");
        }
        sb.delete(sb.length()-1, sb.length());
        return sb.toString();
    }

    private Availability isAvailable() {
        Optional<AccountDto> user = accountService.describe();
        return user.isPresent() && user.get().accountType() == AccountType.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }
}
