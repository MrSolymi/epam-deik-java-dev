package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dto.RoomDto;
import com.epam.training.ticketservice.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.exceptions.NotFoundException;

import java.util.List;

public interface RoomService {

    void createRoom(RoomDto roomDto) throws AlreadyExistsException;
    void updateRoom(String name, int numberOfRows, int numberOfColumns) throws NotFoundException;
    void deleteRoom(String name) throws NotFoundException;
    List<RoomDto> getRoomList();
}
