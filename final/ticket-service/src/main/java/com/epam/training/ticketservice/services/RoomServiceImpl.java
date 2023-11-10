package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dto.RoomDto;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;

    @Override
    public void createRoom(RoomDto roomDto) {
        Room room = new Room(
                roomDto.getName(),
                roomDto.getNumberOfRows(),
                roomDto.getNumberOfColumns()
        );
        roomRepository.save(room);
    }

    @Override
    public void updateRoom(String name, int numberOfRows, int numberOfColumns) {
        Optional<Room> room = roomRepository.findByName(name);
        if (room.isEmpty())
            return;
        room.get().setNumberOfRows(numberOfRows);
        room.get().setNumberOfColumns(numberOfColumns);
        roomRepository.save(room.get());
    }

    @Override
    public void deleteRoom(String name) {
        Optional<Room> room = roomRepository.deleteByName(name);
        if (room.isEmpty())
            return;
        roomRepository.deleteByName(name);
    }

    @Override
    public List<RoomDto> getRoomList() {
        return roomRepository.findAll().stream().map(RoomDto::new).toList();
    }
}
