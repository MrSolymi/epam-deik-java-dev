package com.epam.training.ticketservice.dto;

import com.epam.training.ticketservice.model.Room;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoomDto {

    private final String name;
    private final int numberOfRows;
    private final int numberOfColumns;

    public RoomDto(Room room) {
        this.name = room.getName();
        this.numberOfRows = room.getNumberOfRows();
        this.numberOfColumns = room.getNumberOfColumns();
    }

    @Override
    public String toString() {
        return "Room " + name + " with " + numberOfRows * numberOfColumns + " seats, " + numberOfRows + " rows and " + numberOfColumns + " columns";
    }
}
