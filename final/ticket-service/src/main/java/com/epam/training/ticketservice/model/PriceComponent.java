package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
public class PriceComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true)
    private String componentName;

    @Column(name = "price")
    private int componentValue;

    @OneToMany()
    @JoinTable(name = "price_component_rooms",
            joinColumns = @JoinColumn(name = "price_component_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private List<Room> rooms;

    @OneToMany()
    @JoinTable(name = "price_component_movies",
            joinColumns = @JoinColumn(name = "price_component_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private List<Movie> movies;

    @OneToMany()
    @JoinTable(name = "price_component_screenings",
            joinColumns = @JoinColumn(name = "price_component_id"),
            inverseJoinColumns = @JoinColumn(name = "screening_id"))
    private List<Screening> screenings;

    public PriceComponent(String componentName, int componentValue) {
        this.componentName = componentName;
        this.componentValue = componentValue;
    }

}
