import com.epam.training.ticketservice.commands.ScreeningCommands;
import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.dto.ScreeningDto;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.exceptions.ScreeningOverlappingException;
import com.epam.training.ticketservice.model.AccountType;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.services.AccountService;
import com.epam.training.ticketservice.services.ScreeningService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.shell.Availability;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScreeningCommandsTest {
    @Mock
    private AccountService accountService;

    @Mock
    private ScreeningService screeningService;

    @InjectMocks
    private ScreeningCommands screeningCommands;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Test
    public void testCreateScreening_SuccessfulCreation() throws ScreeningOverlappingException, NotFoundException {
        String movieTitle = "Movie 1";
        String roomName = "Room 1";
        String startDate = "2023-12-01 18:00";

        String result = screeningCommands.createScreening(movieTitle, roomName, startDate);

        verify(screeningService, times(1)).createScreening(movieTitle, roomName, LocalDateTime.parse(startDate, formatter));
        assertEquals("Successfully created screening", result);
    }

    @Test
    public void testCreateScreening_FailedCreation() throws ScreeningOverlappingException, NotFoundException {
        String movieTitle = "Movie 1";
        String roomName = "Room 1";
        String startDate = "2023-12-01 18:00";

        doThrow(new RuntimeException("Screening creation failed")).when(screeningService).createScreening(any(), any(), any());

        String result = screeningCommands.createScreening(movieTitle, roomName, startDate);

        verify(screeningService, times(1)).createScreening(movieTitle, roomName, LocalDateTime.parse(startDate, formatter));
        assertEquals("Screening creation failed", result);
    }

    @Test
    public void testDeleteScreening_SuccessfulDeletion() throws NotFoundException {
        String movieTitle = "Movie 1";
        String roomName = "Room 1";
        String startDate = "2023-12-01 18:00";

        String result = screeningCommands.deleteScreening(movieTitle, roomName, startDate);

        verify(screeningService, times(1)).deleteScreening(movieTitle, roomName, LocalDateTime.parse(startDate, formatter));
        assertEquals("Successfully deleted screening", result);
    }

    @Test
    public void testDeleteScreening_FailedDeletion() throws NotFoundException {
        String movieTitle = "Movie 1";
        String roomName = "Room 1";
        String startDate = "2023-12-01 18:00";

        doThrow(new RuntimeException("Screening deletion failed")).when(screeningService).deleteScreening(any(), any(), any());

        String result = screeningCommands.deleteScreening(movieTitle, roomName, startDate);

        verify(screeningService, times(1)).deleteScreening(movieTitle, roomName, LocalDateTime.parse(startDate, formatter));
        assertEquals("Screening deletion failed", result);
    }

    @Test
    public void testGetScreeningList_NoScreenings() {
        when(screeningService.getScreeningList()).thenReturn(new ArrayList<>());

        String result = screeningCommands.getScreeningList();

        assertEquals("There are no screenings", result);
    }

    @Test
    public void testGetScreeningList_WithScreenings() {
        List<ScreeningDto> screeningList = new ArrayList<>();
        Movie movie = new Movie("Movie 1", "Action", 120);
        Room room = new Room("Room 1", 10, 10);
        Screening screening1 = new Screening(movie, room, LocalDateTime.parse("2000-12-20 18:00", formatter));
        Screening screening2 = new Screening(movie, room, LocalDateTime.parse("2001-12-20 18:00", formatter));



        screeningList.add(new ScreeningDto(screening1));
        screeningList.add(new ScreeningDto(screening2));

        when(screeningService.getScreeningList()).thenReturn(screeningList);

        String result = screeningCommands.getScreeningList();

        StringBuilder sb = new StringBuilder();
        for (var item : screeningList) {
            sb.append(item).append("\n");
        }
        sb.delete(sb.length() - 1, sb.length());

        assertEquals(sb.toString(), result);
    }




}
