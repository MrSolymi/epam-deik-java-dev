package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dto.RoomDto;
import com.epam.training.ticketservice.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.exceptions.NotFoundException;
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

    private static final String ROOM_AlREADY_EXIST = "The room with the given name already exist!";
    private static final String ROOM_NOT_FOUND = "Room with given name not found.";

    @Override
    public void createRoom(RoomDto roomDto) throws AlreadyExistsException {
        if (roomRepository.findByName(roomDto.getName()).isPresent())
            throw new AlreadyExistsException(ROOM_AlREADY_EXIST);
        Room room = new Room(
                roomDto.getName(),
                roomDto.getNumberOfRows(),
                roomDto.getNumberOfColumns()
        );
        roomRepository.save(room);
    }

    @Override
    public void updateRoom(String name, int numberOfRows, int numberOfColumns) throws NotFoundException {
        Optional<Room> room = roomRepository.findByName(name);
        if (room.isEmpty())
            throw new NotFoundException(ROOM_NOT_FOUND);
        room.get().setNumberOfRows(numberOfRows);
        room.get().setNumberOfColumns(numberOfColumns);
        roomRepository.save(room.get());
    }

    @Override
    public void deleteRoom(String name) throws NotFoundException {
        Optional<Room> room = roomRepository.findByName(name);
        if (room.isEmpty())
            throw new NotFoundException(ROOM_NOT_FOUND);
        roomRepository.deleteByName(name);
    }

    @Override
    public List<RoomDto> getRoomList() {
        return roomRepository.findAll().stream().map(RoomDto::new).toList();
    }
}
