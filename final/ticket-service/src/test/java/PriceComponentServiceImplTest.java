import com.epam.training.ticketservice.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.PriceComponent;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.repositories.MovieRepository;
import com.epam.training.ticketservice.repositories.PriceComponentRepository;
import com.epam.training.ticketservice.repositories.RoomRepository;
import com.epam.training.ticketservice.repositories.ScreeningRepository;
import com.epam.training.ticketservice.services.impl.PriceComponentServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PriceComponentServiceImplTest {
    @Mock
    private PriceComponentRepository priceComponentRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ScreeningRepository screeningRepository;

    @InjectMocks
    private PriceComponentServiceImpl priceComponentService;

    @Test
    public void testCreatePriceComponent() {
        when(priceComponentRepository.findByComponentName(anyString())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> priceComponentService.createPriceComponent("ComponentName", 100));
    }

    @Test
    public void testCreatePriceComponent_AlreadyExistsExceptionThrown() {
        when(priceComponentRepository.findByComponentName(anyString())).thenReturn(Optional.of(new PriceComponent()));

        assertThrows(AlreadyExistsException.class, () -> priceComponentService.createPriceComponent("ComponentName", 100));
    }

    @Test
    public void testPriceComponentToRoom() {
        PriceComponent priceComponent = new PriceComponent("ComponentName", 100);
        Room room = new Room("RoomName", 10, 10);
        when(priceComponentRepository.findByComponentName(anyString())).thenReturn(Optional.of(priceComponent));
        when(roomRepository.findByName(anyString())).thenReturn(Optional.of(room));

        priceComponent.setRooms(new ArrayList<>());

        assertDoesNotThrow(() -> priceComponentService.priceComponentToRoom("ComponentName", "RoomName"));
    }
    @Test
    public void testPriceComponentToRoom_RoomNotFoundExceptionThrown() {
        PriceComponent priceComponent = new PriceComponent("ComponentName", 100);
        Room room = new Room("RoomName", 10, 10);
        when(priceComponentRepository.findByComponentName(anyString())).thenReturn(Optional.of(priceComponent));
        when(roomRepository.findByName(anyString())).thenReturn(Optional.empty());

        priceComponent.setRooms(new ArrayList<>());

        assertThrows(NotFoundException.class, () -> priceComponentService.priceComponentToRoom("ComponentName", "RoomName"));
    }

    @Test
    public void testPriceComponentToRoom_PriceComponentNotFoundExceptionThrown() {
        when(priceComponentRepository.findByComponentName(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> priceComponentService.priceComponentToRoom("ComponentName", "RoomName"));
    }

    @Test
    public void testPriceComponentToMovie() {
        PriceComponent priceComponent = new PriceComponent("ComponentName", 100);
        Movie movie = new Movie("MovieTitle", "Action", 120);
        when(priceComponentRepository.findByComponentName(anyString())).thenReturn(Optional.of(priceComponent));
        when(movieRepository.findByTitle(anyString())).thenReturn(Optional.of(movie));

        assertDoesNotThrow(() -> priceComponentService.priceComponentToMovie("ComponentName", "MovieTitle"));
    }

    @Test
    public void testPriceComponentToMovie_PriceComponentNotFoundExceptionThrown() {
        when(priceComponentRepository.findByComponentName(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> priceComponentService.priceComponentToMovie("ComponentName", "MovieTitle"));
    }

    @Test
    public void testPriceComponentToMovie_MovieNotFoundExceptionThrown() {
        when(priceComponentRepository.findByComponentName(anyString())).thenReturn(Optional.of(new PriceComponent()));
        when(movieRepository.findByTitle(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> priceComponentService.priceComponentToMovie("ComponentName", "MovieTitle"));
    }

    @Test
    public void testPriceComponentToScreening() {
        PriceComponent priceComponent = new PriceComponent("ComponentName", 100);
        Movie movie = new Movie("MovieTitle", "Action", 120);
        Room room = new Room("RoomName", 10, 10);
        LocalDateTime dateTime = LocalDateTime.now();
        Screening screening = new Screening(movie, room, dateTime);
        when(priceComponentRepository.findByComponentName(anyString())).thenReturn(Optional.of(priceComponent));
        when(movieRepository.findByTitle(anyString())).thenReturn(Optional.of(movie));
        when(roomRepository.findByName(anyString())).thenReturn(Optional.of(room));
        when(screeningRepository.findScreeningByMovieAndRoomAndDate(any(), any(), any())).thenReturn(Optional.of(screening));

        assertDoesNotThrow(() -> priceComponentService.priceComponentToScreening("ComponentName", "MovieTitle", "RoomName", dateTime));
    }

    @Test
    public void testPriceComponentToScreening_PriceComponentNotFoundExceptionThrown() {
        when(priceComponentRepository.findByComponentName(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> priceComponentService.priceComponentToScreening("ComponentName", "MovieTitle", "RoomName", LocalDateTime.now()));
    }

    @Test
    public void testPriceComponentToScreening_MovieNotFoundExceptionThrown() {
        when(priceComponentRepository.findByComponentName(anyString())).thenReturn(Optional.of(new PriceComponent()));
        when(movieRepository.findByTitle("MovieTitle")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> priceComponentService.priceComponentToScreening("ComponentName", "MovieTitle", "RoomName", LocalDateTime.now()));
    }

    @Test
    public void testPriceComponentToScreening_RoomNotFoundExceptionThrown() {
        when(priceComponentRepository.findByComponentName(anyString())).thenReturn(Optional.of(new PriceComponent()));
        when(movieRepository.findByTitle("MovieTitle")).thenReturn(Optional.of(new Movie()));
        when(roomRepository.findByName("RoomName")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> priceComponentService.priceComponentToScreening("ComponentName", "MovieTitle", "RoomName", LocalDateTime.now()));
    }

    @Test
    public void testPriceComponentToScreening_ScreeningNotFoundExceptionThrown() {
        Movie movie = new Movie("MovieTitle", "Action", 120);
        Room room = new Room("RoomName", 10, 10);
        LocalDateTime dateTime = LocalDateTime.now();

        when(priceComponentRepository.findByComponentName(anyString())).thenReturn(Optional.of(new PriceComponent()));
        when(movieRepository.findByTitle("MovieTitle")).thenReturn(Optional.of(movie));
        when(roomRepository.findByName("RoomName")).thenReturn(Optional.of(room));
        when(screeningRepository.findScreeningByMovieAndRoomAndDate(movie, room, dateTime)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> priceComponentService.priceComponentToScreening("ComponentName", "MovieTitle", "RoomName", dateTime));
    }

    @Test
    public void testCalculateAdditionalPrices() throws NotFoundException {
        PriceComponent roomComponent = new PriceComponent("RoomComponent", 50);
        PriceComponent movieComponent = new PriceComponent("MovieComponent", 30);
        PriceComponent screeningComponent = new PriceComponent("ScreeningComponent", 20);

        when(priceComponentRepository.findByComponentName(roomComponent.getComponentName())).thenReturn(Optional.of(roomComponent));
        when(priceComponentRepository.findByComponentName(movieComponent.getComponentName())).thenReturn(Optional.of(movieComponent));
        when(priceComponentRepository.findByComponentName(screeningComponent.getComponentName())).thenReturn(Optional.of(screeningComponent));

        LocalDateTime dateTime = LocalDateTime.now();
        Room room = new Room("RoomName", 10, 10);
        Movie movie = new Movie("MovieTitle", "Action", 120);
        Screening screening = new Screening(movie, room, dateTime);

        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.of(movie));
        when(screeningRepository.findScreeningByMovieAndRoomAndDate(movie, room, dateTime)).thenReturn(Optional.of(screening));

        priceComponentService.priceComponentToRoom(roomComponent.getComponentName(), room.getName());
        priceComponentService.priceComponentToMovie(movieComponent.getComponentName(), movie.getTitle());
        priceComponentService.priceComponentToScreening(screeningComponent.getComponentName(), movie.getTitle(), room.getName(), dateTime);


        List<PriceComponent> components = new ArrayList<>();

        components.add(roomComponent);
        components.add(movieComponent);
        components.add(screeningComponent);

        when(priceComponentRepository.findAll()).thenReturn(components);

        int additionalPrice = priceComponentService.calculateAdditionalPrices(screening);

        assertEquals(100, additionalPrice);
    }

    @Test
    public void testCalculateAdditionalPrices_NoRoomComponents() throws NotFoundException {
        PriceComponent movieComponent = new PriceComponent("MovieComponent", 30);
        PriceComponent screeningComponent = new PriceComponent("ScreeningComponent", 20);

        when(priceComponentRepository.findByComponentName(movieComponent.getComponentName())).thenReturn(Optional.of(movieComponent));
        when(priceComponentRepository.findByComponentName(screeningComponent.getComponentName())).thenReturn(Optional.of(screeningComponent));

        LocalDateTime dateTime = LocalDateTime.now();
        Room room = new Room("RoomName", 10, 10);
        Movie movie = new Movie("MovieTitle", "Action", 120);
        Screening screening = new Screening(movie, room, dateTime);

        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.of(movie));
        when(screeningRepository.findScreeningByMovieAndRoomAndDate(movie, room, dateTime)).thenReturn(Optional.of(screening));

        priceComponentService.priceComponentToMovie(movieComponent.getComponentName(), movie.getTitle());
        priceComponentService.priceComponentToScreening(screeningComponent.getComponentName(), movie.getTitle(), room.getName(), dateTime);


        List<PriceComponent> components = new ArrayList<>();

        components.add(movieComponent);
        components.add(screeningComponent);

        when(priceComponentRepository.findAll()).thenReturn(components);

        int additionalPrice = priceComponentService.calculateAdditionalPrices(screening);

        assertEquals(50, additionalPrice);
    }

    @Test
    public void testCalculateAdditionalPrices_NoMovieComponents() throws NotFoundException {
        PriceComponent roomComponent = new PriceComponent("RoomComponent", 50);
        PriceComponent screeningComponent = new PriceComponent("ScreeningComponent", 20);

        when(priceComponentRepository.findByComponentName(roomComponent.getComponentName())).thenReturn(Optional.of(roomComponent));
        when(priceComponentRepository.findByComponentName(screeningComponent.getComponentName())).thenReturn(Optional.of(screeningComponent));

        LocalDateTime dateTime = LocalDateTime.now();
        Room room = new Room("RoomName", 10, 10);
        Movie movie = new Movie("MovieTitle", "Action", 120);
        Screening screening = new Screening(movie, room, dateTime);

        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.of(movie));
        when(screeningRepository.findScreeningByMovieAndRoomAndDate(movie, room, dateTime)).thenReturn(Optional.of(screening));

        priceComponentService.priceComponentToRoom(roomComponent.getComponentName(), room.getName());
        priceComponentService.priceComponentToScreening(screeningComponent.getComponentName(), movie.getTitle(), room.getName(), dateTime);


        List<PriceComponent> components = new ArrayList<>();

        components.add(roomComponent);
        components.add(screeningComponent);

        when(priceComponentRepository.findAll()).thenReturn(components);

        int additionalPrice = priceComponentService.calculateAdditionalPrices(screening);

        assertEquals(70, additionalPrice);
    }

    @Test
    public void testCalculateAdditionalPrices_NoScreeningComponents() throws NotFoundException {
        PriceComponent roomComponent = new PriceComponent("RoomComponent", 50);
        PriceComponent movieComponent = new PriceComponent("MovieComponent", 30);

        when(priceComponentRepository.findByComponentName(roomComponent.getComponentName())).thenReturn(Optional.of(roomComponent));
        when(priceComponentRepository.findByComponentName(movieComponent.getComponentName())).thenReturn(Optional.of(movieComponent));

        LocalDateTime dateTime = LocalDateTime.now();
        Room room = new Room("RoomName", 10, 10);
        Movie movie = new Movie("MovieTitle", "Action", 120);
        Screening screening = new Screening(movie, room, dateTime);

        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.of(movie));

        priceComponentService.priceComponentToRoom(roomComponent.getComponentName(), room.getName());
        priceComponentService.priceComponentToMovie(movieComponent.getComponentName(), movie.getTitle());


        List<PriceComponent> components = new ArrayList<>();

        components.add(roomComponent);
        components.add(movieComponent);

        when(priceComponentRepository.findAll()).thenReturn(components);

        int additionalPrice = priceComponentService.calculateAdditionalPrices(screening);

        assertEquals(80, additionalPrice);
    }
}
