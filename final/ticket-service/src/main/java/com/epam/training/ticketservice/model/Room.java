package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Room {

    private String name;
    private int numberOfRows, numberOfColumns;

    @Override
    public String toString() {
        return "Room " + name + " with " + numberOfRows * numberOfColumns + " seats, " + numberOfRows + " rows and " + numberOfColumns + " columns";
    }
}
