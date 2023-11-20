package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.awt.print.Book;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "screening_id", referencedColumnName = "id")
    private Screening screening;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Seat> seats;

    private int price;

    public Booking(Account account, Screening screening, List<Seat> seats) {
        this.account = account;
        this.screening = screening;
        this.seats = seats;
    }

    public Booking(Screening screening, List<Seat> seats, int price) {
        this.screening = screening;
        this.seats = seats;
        this.price = price;
    }
}
