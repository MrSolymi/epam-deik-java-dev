package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.exceptions.AlreadyExistsException;

public interface PriceComponentService {

    void createPriceComponent(String componentName, int componentValue) throws AlreadyExistsException;
}
