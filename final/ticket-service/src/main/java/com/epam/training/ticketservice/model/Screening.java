package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;

    @Column(name = "date")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "component_id", referencedColumnName = "id")
    private PriceComponent priceComponent;

    public Screening(Movie movie, Room room, LocalDateTime date) {
        this.movie = movie;
        this.room = room;
        this.date = date;
    }
}
