package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Movie {

    private String title, type;
    private int length;

    @Override
    public String toString() {
        return title + " (" + type + ", " + length + " minutes)";
    }
}
