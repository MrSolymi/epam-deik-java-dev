package com.epam.training.ticketservice.repositories;

import com.epam.training.ticketservice.model.PriceComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceComponentRepository extends JpaRepository<PriceComponent, Long> {
    Optional<PriceComponent> findByComponentName(String componentName);
}
