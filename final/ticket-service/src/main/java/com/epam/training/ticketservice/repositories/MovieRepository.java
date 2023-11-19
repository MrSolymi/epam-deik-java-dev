package com.epam.training.ticketservice.repositories;

import com.epam.training.ticketservice.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Transactional
    void deleteByTitle(String title);

    Optional<Movie> findByTitle(String title);
}
