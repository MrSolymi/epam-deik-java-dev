package com.epam.training.ticketservice.dto;

import com.epam.training.ticketservice.model.PriceComponent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PriceComponentDto {

    private final String componentName;
    private final int componentValue;

    public PriceComponentDto(PriceComponent component) {
        componentName = component.getComponentName();
        componentValue = component.getComponentValue();
    }
}
