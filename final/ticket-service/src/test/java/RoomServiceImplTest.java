import com.epam.training.ticketservice.dto.RoomDto;
import com.epam.training.ticketservice.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.repositories.RoomRepository;
import com.epam.training.ticketservice.services.impl.RoomServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceImplTest {
    @Mock
    RoomRepository roomRepository;

    @InjectMocks
    RoomServiceImpl roomService;

    @Test
    public void testCreateRoomShouldThrowAlreadyExistsException() {
        Room room = new Room("Room", 10, 10);
        Mockito.when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));

        assertThrows(AlreadyExistsException.class, () -> {
            roomService.createRoom(room.getName(), room.getNumberOfRows(), room.getNumberOfColumns());
        });
    }

    @Test
    public void testCreateRoom() throws AlreadyExistsException {
        Room room = new Room("Room1", 10, 10);

        roomService.createRoom(room.getName(), room.getNumberOfRows(), room.getNumberOfColumns());
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    public void testUpdateRoomShouldThrowNotFoundException() {
        Room room = new Room("NonexistentRoom", 10, 10);
        Mockito.when(roomRepository.findByName(room.getName())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
            roomService.updateRoom(room.getName(), room.getNumberOfRows(), room.getNumberOfColumns())
        );
    }

    @Test
    public void testUpdateRoom() throws NotFoundException, AlreadyExistsException {
        when(roomRepository.findByName(any())).thenReturn(Optional.of(new Room()));
        var existingRoom = roomRepository.findByName("Room1");

        assertDoesNotThrow(() -> roomService.updateRoom(existingRoom.get().getName(), existingRoom.get().getNumberOfRows(), existingRoom.get().getNumberOfColumns()));

        //roomService.updateRoom("room.getName()", 10, 10);
        verify(roomRepository, times(1)).save(existingRoom.get());
    }

    @Test
    public void testDeleteRoom() throws NotFoundException {
        Room existingRoom = new Room("ExistingRoom", 5, 5);
        when(roomRepository.findByName(existingRoom.getName())).thenReturn(Optional.of(existingRoom));

        // Act
        assertDoesNotThrow(() -> roomService.deleteRoom(existingRoom.getName()));

        // Assert
        // Verify that the deleteByName method was called with the expected room name
        verify(roomRepository).deleteByName(existingRoom.getName());
    }

    @Test
    public void testDeleteRoomNotFound() {
        String roomName = "NonExistingRoom";
        when(roomRepository.findByName("NonExistingRoom")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            roomService.deleteRoom(roomName);
        });

        Mockito.verify(roomRepository, Mockito.never()).deleteByName(Mockito.any());
    }
    @Test
    void testGetRoomList() {
        Room room1 = new Room("Room1", 10, 10);
        Room room2 = new Room("Room2", 12, 8);

        List<Room> rooms = List.of(room1, room2);
        when(roomRepository.findAll()).thenReturn(rooms);

        List<RoomDto> roomDtos = roomService.getRoomList();

        assertEquals(2, roomDtos.size());
        assertEquals("Room1", roomDtos.get(0).getName());
        assertEquals(10, roomDtos.get(0).getNumberOfRows());
        assertEquals(10, roomDtos.get(0).getNumberOfColumns());
        assertEquals("Room2", roomDtos.get(1).getName());
        assertEquals(12, roomDtos.get(1).getNumberOfRows());
        assertEquals(8, roomDtos.get(1).getNumberOfColumns());
    }

}
