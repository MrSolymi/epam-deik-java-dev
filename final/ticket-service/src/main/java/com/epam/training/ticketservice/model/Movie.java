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
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    private String type;

    private int length;

    @ManyToOne
    @JoinColumn(name = "component_id", referencedColumnName = "id")
    private PriceComponent priceComponent;

    public Movie(String title, String type, int length) {
        this.title = title;
        this.type = type;
        this.length = length;
    }

}
