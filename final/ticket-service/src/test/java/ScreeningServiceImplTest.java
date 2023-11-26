import com.epam.training.ticketservice.dto.ScreeningDto;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.exceptions.ScreeningOverlappingException;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.repositories.MovieRepository;
import com.epam.training.ticketservice.repositories.RoomRepository;
import com.epam.training.ticketservice.repositories.ScreeningRepository;
import com.epam.training.ticketservice.services.impl.ScreeningServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScreeningServiceImplTest {

    @Mock
    ScreeningRepository screeningRepository;
    @Mock
    MovieRepository movieRepository;
    @Mock
    RoomRepository roomRepository;

    @InjectMocks
    ScreeningServiceImpl screeningService;

    @Test
    public void testCreateScreening() {
        String movieTitle = "TestMovie";
        String roomName = "TestRoom";
        LocalDateTime dateTime = LocalDateTime.now();

        Movie existingMovie = new Movie(movieTitle, "Action", 120);
        when(movieRepository.findByTitle(movieTitle)).thenReturn(Optional.of(existingMovie));

        Room existingRoom = new Room(roomName, 10, 10);
        when(roomRepository.findByName(roomName)).thenReturn(Optional.of(existingRoom));

        when(screeningRepository.findAllByRoom(existingRoom)).thenReturn(List.of());

        assertDoesNotThrow(() -> screeningService.createScreening(movieTitle, roomName, dateTime));

        verify(screeningRepository).save(Mockito.argThat(screening ->
                screening.getMovie().equals(existingMovie)
                        && screening.getRoom().equals(existingRoom)
                        && screening.getDate().equals(dateTime)
        ));
    }

    @Test
    public void testGetScreeningList() {
        List<Screening> screenings = Arrays.asList(
                new Screening(new Movie(), new Room(), LocalDateTime.now()),
                new Screening(new Movie(), new Room(), LocalDateTime.now())
        );
        when(screeningRepository.findAll()).thenReturn(screenings);

        List<ScreeningDto> result = screeningService.getScreeningList();

        assertEquals(screenings.size(), result.size());
    }
    @Test
    void testCreateScreeningMovieNotFound() {
        String nonExistingTitle = "NonExistingMovie";
        String existingRoomName = "ExistingRoom";
        LocalDateTime dateTime = LocalDateTime.now();

        when(movieRepository.findByTitle(nonExistingTitle)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            screeningService.createScreening(nonExistingTitle, existingRoomName, dateTime);
        });

        assertEquals("The movie with the given title is not found", exception.getMessage());

        verify(screeningRepository, never()).save(any());
    }

    @Test
    void testCreateScreeningRoomNotFound() {
        String existingTitle = "ExistingMovie";
        String nonExistingRoomName = "NonExistingRoom";
        LocalDateTime dateTime = LocalDateTime.now();

        when(movieRepository.findByTitle(existingTitle)).thenReturn(Optional.of(new Movie(existingTitle, "Action", 120)));
        when(roomRepository.findByName(nonExistingRoomName)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            screeningService.createScreening(existingTitle, nonExistingRoomName, dateTime);
        });

        assertEquals("The room with the given name is not found", exception.getMessage());

        verify(screeningRepository, never()).save(any());
    }

    @Test
    void testDeleteScreening() {
        String movieTitle = "ExistingMovie";
        String roomName = "ExistingRoom";
        LocalDateTime startTime = LocalDateTime.now();

        Movie existingMovie = new Movie(movieTitle, "Action", 120);
        when(movieRepository.findByTitle(movieTitle)).thenReturn(Optional.of(existingMovie));

        Room existingRoom = new Room(roomName, 10, 10);
        when(roomRepository.findByName(roomName)).thenReturn(Optional.of(existingRoom));

        Screening existingScreening = new Screening(existingMovie, existingRoom, startTime);
        when(screeningRepository.findScreeningByMovieAndRoomAndDate(existingMovie, existingRoom, startTime))
                .thenReturn(Optional.of(existingScreening));

        assertDoesNotThrow(() -> screeningService.deleteScreening(movieTitle, roomName, startTime));

        verify(screeningRepository).delete(existingScreening);
    }

    @Test
    void testDeleteScreeningNotFound() {
        String nonExistingMovieTitle = "NonExistingMovie";
        String roomName = "NonExistingRoom";
        LocalDateTime startTime = LocalDateTime.now();

        when(movieRepository.findByTitle(nonExistingMovieTitle)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            screeningService.deleteScreening(nonExistingMovieTitle, roomName, startTime);
        });

        assertEquals("The movie with the given title is not found", exception.getMessage());

        verify(screeningRepository, never()).delete(any());
    }

    @Test
    void testDeleteScreeningRoomNotFound() {
        String existingMovieTitle = "ExistingMovie";
        String nonExistingRoomName = "NonExistingRoom";
        LocalDateTime startTime = LocalDateTime.now();

        Movie existingMovie = new Movie(existingMovieTitle, "Action", 120);
        when(movieRepository.findByTitle(existingMovieTitle)).thenReturn(Optional.of(existingMovie));
        when(roomRepository.findByName(nonExistingRoomName)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            screeningService.deleteScreening(existingMovieTitle, nonExistingRoomName, startTime);
        });

        assertEquals("The room with the given name is not found", exception.getMessage());

        verify(screeningRepository, never()).delete(any());
    }

    @Test
    void testDeleteScreeningScreeningNotFound() {
        String existingMovieTitle = "ExistingMovie";
        String existingRoomName = "ExistingRoom";
        LocalDateTime startTime = LocalDateTime.now();

        Movie existingMovie = new Movie(existingMovieTitle, "Action", 120);
        when(movieRepository.findByTitle(existingMovieTitle)).thenReturn(Optional.of(existingMovie));

        Room existingRoom = new Room(existingRoomName, 10, 10);
        when(roomRepository.findByName(existingRoomName)).thenReturn(Optional.of(existingRoom));

        when(screeningRepository.findScreeningByMovieAndRoomAndDate(existingMovie, existingRoom, startTime))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            screeningService.deleteScreening(existingMovieTitle, existingRoomName, startTime);
        });

        assertEquals("The screening with the given data is not found", exception.getMessage());

        verify(screeningRepository, never()).delete(any());
    }

    @Test
    void testDeleteScreeningSuccess() {
        String existingMovieTitle = "ExistingMovie";
        String existingRoomName = "ExistingRoom";
        LocalDateTime startTime = LocalDateTime.now();

        Movie existingMovie = new Movie(existingMovieTitle, "Action", 120);
        when(movieRepository.findByTitle(existingMovieTitle)).thenReturn(Optional.of(existingMovie));

        Room existingRoom = new Room(existingRoomName, 10, 10);
        when(roomRepository.findByName(existingRoomName)).thenReturn(Optional.of(existingRoom));

        Screening existingScreening = new Screening(existingMovie, existingRoom, startTime);
        when(screeningRepository.findScreeningByMovieAndRoomAndDate(existingMovie, existingRoom, startTime))
                .thenReturn(Optional.of(existingScreening));

        assertDoesNotThrow(() -> screeningService.deleteScreening(existingMovieTitle, existingRoomName, startTime));

        verify(screeningRepository).delete(existingScreening);
    }

    @Test
    public void testOverlapValidate_Overlap() {
        Movie movie = new Movie("Movie 1", "Action", 120);
        Room room = new Room("Room 1", 10, 10);

        Screening previousScreening = new Screening(
                movie,
                room,
                LocalDateTime.parse("2023-01-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );

        Screening screening = new Screening(
                movie,
                room,
                LocalDateTime.parse("2023-01-01 10:10", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );

        List<Screening> screenings = new ArrayList<>();
        screenings.add(previousScreening);

        when(screeningRepository.findAllByRoom(room)).thenReturn(screenings);

        assertThrows(ScreeningOverlappingException.class, () -> screeningService.overlapValidate(screening));
    }
}
