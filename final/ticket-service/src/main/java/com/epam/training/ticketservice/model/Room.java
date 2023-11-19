package com.epam.training.ticketservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@Data
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private int numberOfRows;

    private int numberOfColumns;

    public Room(String name, int numberOfRows, int numberOfColumns) {
        this.name = name;
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
    }
}
