package com.epam.training.ticketservice.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class RoomCommands {
    @ShellMethod(key = "create room", value = "Create room by <name> <rows> <columns>")
    public String createRoom(String title, int rows, int columns){
        return String.format("Successfully created room '%s'", title);
    }

    @ShellMethod(key = "update room", value = "Update room by <name> <rows> <columns>")
    public String updateRoom(String title, int rows, int columns){
        return String.format("Successfully updated room '%s'", title);
    }

    @ShellMethod(key = "delete room", value = "Delete room by <name>")
    public String deleteRoom(String title){
        return String.format("Successfully deleted room '%s'", title);
    }

    @ShellMethod(key = "list rooms", value = "List rooms")
    public String listRooms(){
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }
}
