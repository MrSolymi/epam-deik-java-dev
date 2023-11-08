package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Screening {

    private LocalDateTime date;
    private Movie movie;
    private Room room;

    @Override
    public String toString() {
        return movie + ", screened in room " + room.getName() + ", at " + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
