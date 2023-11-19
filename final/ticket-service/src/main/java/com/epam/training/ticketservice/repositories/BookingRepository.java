package com.epam.training.ticketservice.repositories;

import com.epam.training.ticketservice.dto.BookingDto;
import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.Booking;
import com.epam.training.ticketservice.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByAccount(Account account);

    List<Booking> findAllByScreening(Screening screening);
}
