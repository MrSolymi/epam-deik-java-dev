package com.epam.training.ticketservice.components;

import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.services.PriceComponentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Calculator {
    private final PriceComponentService priceComponentService;

    @Getter
    private static int basePrice = 1500;

    public int calculate(Screening screening, int numberOfTickets) {
        return numberOfTickets * (basePrice + priceComponentService.calculateAdditionalPrices(screening));
    }

    public static void setBasePrice(int basePrice) {
        Calculator.basePrice = basePrice;
    }
}
