package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int numberOfRows;

    private int numberOfColumns;

    @ManyToOne
    @JoinColumn(name = "component_id", referencedColumnName = "id")
    private PriceComponent priceComponent;

    public Room(String name, int numberOfRows, int numberOfColumns) {
        this.name = name;
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
    }
}
