import com.epam.training.ticketservice.commands.RoomCommands;
import com.epam.training.ticketservice.dto.RoomDto;
import com.epam.training.ticketservice.services.AccountService;
import com.epam.training.ticketservice.services.RoomService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomCommandsTest {
    @Mock
    private AccountService accountService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomCommands roomCommands;


    @Test
    public void testGetRoomList_NoRooms() {
        when(roomService.getRoomList()).thenReturn(new ArrayList<>());

        String result = roomCommands.getRoomList();

        assertEquals("There are no rooms at the moment", result);
    }

    @Test
    public void testGetRoomList_WithRooms() {
        List<RoomDto> roomList = new ArrayList<>();
        roomList.add(new RoomDto("Room 1", 10, 10));
        roomList.add(new RoomDto("Room 2", 8, 12));

        when(roomService.getRoomList()).thenReturn(roomList);

        String result = roomCommands.getRoomList();

        StringBuilder expected = new StringBuilder();
        for (var room : roomList) {
            expected.append(room).append("\n");
        }
        expected.delete(expected.length() - 1, expected.length());

        assertEquals(expected.toString(), result);
    }
}
