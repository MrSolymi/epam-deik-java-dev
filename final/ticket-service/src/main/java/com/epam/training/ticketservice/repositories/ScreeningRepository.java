package com.epam.training.ticketservice.repositories;

import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    Optional<Screening> findScreeningByMovieAndRoomAndDate(Movie movie, Room room, LocalDateTime date);
    List<Screening> findAllByRoom(Room room);


}
