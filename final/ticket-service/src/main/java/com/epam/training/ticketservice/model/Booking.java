package com.epam.training.ticketservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    public Booking(Account account, Screening screening, List<Seat> seats) {
        this.account = account;
        this.screening = screening;
        this.seats = seats;
    }
}
