package com.epam.training.ticketservice.dto;

import com.epam.training.ticketservice.model.AccountType;

public record AccountDto(String username, AccountType accountType) {
}
