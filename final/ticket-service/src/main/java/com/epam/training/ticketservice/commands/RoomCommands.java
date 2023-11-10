package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.dto.RoomDto;
import com.epam.training.ticketservice.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.model.AccountType;
import com.epam.training.ticketservice.services.AccountService;
import com.epam.training.ticketservice.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class RoomCommands {

    private final AccountService accountService;
    private final RoomService roomService;

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create room", value = "Create room by <name> <rows> <columns>")
    public String createRoom(String name, int rows, int columns) throws AlreadyExistsException {
        RoomDto roomDto = new RoomDto(name, rows, columns);
        roomService.createRoom(roomDto);
        return String.format("Successfully created room '%s'", name);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update room", value = "Update room by <name> <rows> <columns>")
    public String updateRoom(String name, int rows, int columns) throws NotFoundException {
        roomService.updateRoom(name, rows, columns);
        return String.format("Successfully updated room '%s'", name);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete room", value = "Delete room by <name>")
    public String deleteRoom(String name) throws NotFoundException {
        roomService.deleteRoom(name);
        return String.format("Successfully deleted room '%s'", name);
    }

    @ShellMethod(key = "list rooms", value = "List rooms")
    public String getRoomList(){
        StringBuilder sb = new StringBuilder();
        var list = roomService.getRoomList();
        if (list.isEmpty())
            return "There are no rooms at the moment";
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
