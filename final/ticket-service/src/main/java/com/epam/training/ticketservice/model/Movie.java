package com.epam.training.ticketservice.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    private String type;

    private int length;

    public Movie(String title, String type, int length) {
        this.title = title;
        this.type = type;
        this.length = length;
    }

}
