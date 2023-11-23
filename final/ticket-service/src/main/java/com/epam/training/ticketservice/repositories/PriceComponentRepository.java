package com.epam.training.ticketservice.repositories;

import com.epam.training.ticketservice.model.PriceComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PriceComponentRepository extends JpaRepository<PriceComponent, Long> {
    @Transactional
    Optional<PriceComponent> findByComponentName(String componentName);
}
