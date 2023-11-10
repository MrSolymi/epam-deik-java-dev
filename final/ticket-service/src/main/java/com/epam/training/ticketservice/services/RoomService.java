package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dto.RoomDto;

import java.util.List;

public interface RoomService {

    void createRoom(RoomDto roomDto);
    void updateRoom(String name, int numberOfRows, int numberOfColumns);
    void deleteRoom(String name);
    List<RoomDto> getRoomList();
}
