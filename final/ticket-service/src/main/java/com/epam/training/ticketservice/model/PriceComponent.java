package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "components")
@Entity
public class PriceComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String componentName;

    private int componentValue = 1500;

    @OneToMany()
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private List<Room> rooms;

    @OneToMany()
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private List<Movie> movies;

    @OneToMany()
    @JoinColumn(name = "screening_id", referencedColumnName = "id")
    private List<Screening> screenings;

    public PriceComponent(String componentName, int componentValue) {
        this.componentName = componentName;
        this.componentValue = componentValue;
    }

}
