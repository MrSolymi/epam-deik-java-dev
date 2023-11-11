package com.epam.training.ticketservice.repositories;

import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    //Optional<Screening> findScreeningByMovie_IdAndRoom_Id(Long movie_id, Long room_id);
    //Optional<Screening> findByMovie (Movie movie);
}
