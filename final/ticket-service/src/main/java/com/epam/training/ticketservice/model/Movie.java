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

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "type")
    private String type;

    @Column(name = "length")
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
