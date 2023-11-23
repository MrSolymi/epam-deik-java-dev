package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class PriceComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String componentName;

    private int componentValue;

    @OneToMany(mappedBy = "priceComponent")
    private List<Room> rooms;

    @OneToMany(mappedBy = "priceComponent")
    private List<Movie> movies;

    @OneToMany(mappedBy = "priceComponent")
    private List<Screening> screenings;

    public PriceComponent(String componentName, int componentValue) {
        this.componentName = componentName;
        this.componentValue = componentValue;
        rooms = new ArrayList<>();
        movies = new ArrayList<>();
        screenings = new ArrayList<>();
    }

}
