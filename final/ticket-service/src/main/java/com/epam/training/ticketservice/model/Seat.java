package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    int rowIndex;
    int columnIndex;
}
