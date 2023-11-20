package com.epam.training.ticketservice.repositories;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.Booking;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByAccount(Account account);


    //List<Booking> findAllByScreening(Screening screening);

    List<Booking> findAllByScreening_MovieAndScreening_RoomAndScreening_Date(Movie movie,
                                                                             Room room,
                                                                             LocalDateTime dateTime);


}
