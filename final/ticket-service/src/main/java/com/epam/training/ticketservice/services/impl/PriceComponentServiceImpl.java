package com.epam.training.ticketservice.services.impl;

import com.epam.training.ticketservice.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.model.PriceComponent;
import com.epam.training.ticketservice.repositories.PriceComponentRepository;
import com.epam.training.ticketservice.services.PriceComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceComponentServiceImpl implements PriceComponentService {

    private static final String COMPONENT_ALREADY_EXISTS = "The component with the given name is already exists";

    private final PriceComponentRepository priceComponentRepository;

    @Override
    public void createPriceComponent(String componentName, int componentValue) throws AlreadyExistsException {
        Optional<PriceComponent> component = priceComponentRepository.findByComponentName(componentName);
        if (component.isPresent()) {
            throw new AlreadyExistsException(COMPONENT_ALREADY_EXISTS);
        }
        priceComponentRepository.save(new PriceComponent(componentName, componentValue));

    }
}
