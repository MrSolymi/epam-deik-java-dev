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

    @Column(unique = true, name = "title")
    private String title;

    @Column(name = "type")
    private String type;

    @Column(name = "length")
    private int length;

    public Movie(String title, String type, int length) {
        this.title = title;
        this.type = type;
        this.length = length;
    }

}
