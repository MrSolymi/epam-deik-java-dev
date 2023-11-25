import com.epam.training.ticketservice.components.Calculator;
import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.dto.BookingDto;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.exceptions.SeatAlreadyTakenException;
import com.epam.training.ticketservice.model.*;
import com.epam.training.ticketservice.repositories.*;
import com.epam.training.ticketservice.services.impl.BookingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
    @Mock
    BookingRepository bookingRepository;
    @Mock
    MovieRepository movieRepository;
    @Mock
    RoomRepository roomRepository;
    @Mock
    ScreeningRepository screeningRepository;
    @Mock
    AccountRepository accountRepository;
    @Mock
    Calculator calculator;

    @InjectMocks
    BookingServiceImpl bookingServiceImpl;

    @Test
    public void testGetBookingListWithValidAccount() throws NotFoundException {
        String username = "testUser";
        AccountDto accountDto = new AccountDto(username, AccountType.USER);
        Account account = new Account(username, "testPassword", AccountType.USER);

        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(account));
        Booking booking1 = new Booking();
        booking1.setAccount(account);
        Booking booking2 = new Booking();
        booking2.setAccount(account);
        when(bookingRepository.findAllByAccount(account)).thenReturn(List.of(booking1, booking2));
        Movie movie = new Movie("Test Movie", "Action", 120);
        Room room = new Room("Test Room", 20, 20);
        Screening screening1 = new Screening(movie, room, LocalDateTime.now());
        Screening screening2 = new Screening(movie, room, LocalDateTime.now());
        booking1.setScreening(screening1);
        booking2.setScreening(screening2);
        List<BookingDto> result = bookingServiceImpl.getBookingList(accountDto);

        assertEquals(2, result.size());
    }

    @Test
    public void testGetBookingListWithInvalidAccount() {
        String username = "nonexistentUser";
        AccountDto accountDto = new AccountDto(username, AccountType.USER);
        when(accountRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingServiceImpl.getBookingList(accountDto));
    }

    @Test
    public void testCreateBooking() throws SeatAlreadyTakenException, NotFoundException {
        String movieTitle = "Test Movie";
        String roomName = "Room1";
        LocalDateTime startTime = LocalDateTime.now();
        String seatListString = "1,1 2,2 3,3";
        AccountDto accountDto = new AccountDto("testUser", AccountType.USER);

        Movie movie = new Movie("Test Movie", "Action", 120);
        Room room = new Room("Test Room", 20, 20);
        Screening screening = new Screening(movie, room, LocalDateTime.now());
        Account account = new Account(accountDto.username(), "testPassword", AccountType.USER);

        when(movieRepository.findByTitle(movieTitle)).thenReturn(Optional.of(movie));
        when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        when(screeningRepository.findScreeningByMovieAndRoomAndDate(movie, room, startTime)).thenReturn(Optional.of(screening));
        when(accountRepository.findByUsername(accountDto.username())).thenReturn(Optional.of(account));

        bookingServiceImpl.createBooking(movieTitle, roomName, startTime, seatListString, accountDto);
    }

    @Test
    void testCreateBookingShouldThrowMovieNotFoundException() {
        AccountDto accountDto = new AccountDto("testUser", AccountType.USER);
        when(movieRepository.findByTitle(Mockito.anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            bookingServiceImpl.createBooking("NonexistentMovie", "RoomName", LocalDateTime.now(), "1,2", accountDto);
        });
    }

    @Test
    void testCreateBookingShouldThrowRoomNotFoundException() {
        AccountDto accountDto = new AccountDto("testUser", AccountType.USER);
        when(movieRepository.findByTitle(Mockito.anyString())).thenReturn(Optional.of(new Movie()));
        when(roomRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            bookingServiceImpl.createBooking("ExistingMovie", "NonexistentRoom", LocalDateTime.now(), "1,2", accountDto);
        });
    }

    @Test
    void testCreateBookingShouldThrowScreeningNotFoundException() {
        AccountDto accountDto = new AccountDto("testUser", AccountType.USER);
        Movie movie = new Movie("Test Movie", "Action", 120);
        Room room = new Room("Test Room", 20, 20);
        LocalDateTime startTime = LocalDateTime.now();
        when(movieRepository.findByTitle(Mockito.anyString())).thenReturn(Optional.of(movie));
        when(roomRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(room));
        when(screeningRepository.findScreeningByMovieAndRoomAndDate(movie, room, startTime)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            bookingServiceImpl.createBooking("ExistingMovie", "ExistentRoom", startTime, "1,2", accountDto);
        });
    }

    @Test
    void testCreateBookingShouldThrowAccountNotFoundException() {
        AccountDto accountDto = new AccountDto("testUser", AccountType.USER);
        Movie movie = new Movie("Test Movie", "Action", 120);
        Room room = new Room("Test Room", 20, 20);
        LocalDateTime startTime = LocalDateTime.now();
        Screening screening = new Screening(movie, room, startTime);
        when(movieRepository.findByTitle(Mockito.anyString())).thenReturn(Optional.of(movie));
        when(roomRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(room));
        when(screeningRepository.findScreeningByMovieAndRoomAndDate(movie, room, startTime)).thenReturn(Optional.of(screening));
        when(accountRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            bookingServiceImpl.createBooking("ExistingMovie", "ExistentRoom", startTime, "1,2", accountDto);
        });
    }

    //@Test
    //public void testCreateBookingWithInvalidBooking() throws SeatAlreadyTakenException, NotFoundException {
    //    // Arrange
    //    String seatListString = "1,1 2,2 3,3";
    //    Account account = new Account("testname", "testpassword", AccountType.USER);
    //    Movie movie = new Movie("Test Movie", "Action", 120);
    //    Room room = new Room("Test Room", 20, 20);
    //    LocalDateTime startTime = LocalDateTime.now();
    //    Screening screening = new Screening(movie, room, startTime);
//
//
//
//
    //    List<Seat> seatList = new ArrayList<>();
    //    String[] seatsInString = seatListString.split(" ");
    //    for (String item : seatsInString) {
    //        String[] s = item.split(",");
    //        seatList.add(new Seat(Integer.parseInt(s[0]), Integer.parseInt(s[1])));
    //    }
    //    Booking booking = new Booking(account, screening, seatList);
    //    // Assuming canBooking returns false for this case
    //    when(bookingServiceImpl.canBooking(new Booking(account, screening, seatList))).thenReturn(false);
//
    //}

    @Test
    void testShowPriceValidBooking() throws NotFoundException {
        String movieTitle = "Test Movie";
        String roomName = "Room1";
        LocalDateTime dateTime = LocalDateTime.now();
        String seatListInString = "1,1 2,2 3,3";

        Movie movie = new Movie("Test Movie", "Action", 120);
        Room room = new Room("Room1", 10, 10);
        Screening screening = new Screening(movie, room, dateTime);
        List<Seat> seatsToBook = Arrays.asList(new Seat(1, 1), new Seat(2, 2), new Seat(3, 3));

        when(movieRepository.findByTitle(movieTitle)).thenReturn(Optional.of(movie));
        when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        when(screeningRepository.findScreeningByMovieAndRoomAndDate(movie, room, dateTime)).thenReturn(Optional.of(screening));
        when(calculator.calculate(screening, seatsToBook.size())).thenReturn(30);

        int result = bookingServiceImpl.showPrice(movieTitle, roomName, dateTime, seatListInString);

        assertEquals(30, result);
    }

    @Test
    void testShowPriceInvalidMovie() {
        String movieTitle = "NonexistentMovie";
        String roomName = "Room1";
        LocalDateTime dateTime = LocalDateTime.now();
        String seatListInString = "1,1 2,2 3,3";

        when(movieRepository.findByTitle(movieTitle)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingServiceImpl.showPrice(movieTitle, roomName, dateTime, seatListInString));
    }

    @Test
    void testShowPriceInvalidRoom() {
        String roomName = "NonexistentRoom";
        String movieTitle = "ExistentMovie";
        LocalDateTime dateTime = LocalDateTime.now();
        String seatListInString = "1,1 2,2 3,3";

        when(movieRepository.findByTitle(movieTitle)).thenReturn(Optional.of(new Movie()));
        when(roomRepository.findByName(roomName)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingServiceImpl.showPrice(movieTitle, roomName, dateTime, seatListInString));
    }

    @Test
    void testShowPriceInvalidScreening() {
        String roomName = "NonexistentRoom";
        String movieTitle = "ExistentMovie";
        LocalDateTime dateTime = LocalDateTime.now();
        String seatListInString = "1,1 2,2 3,3";

        when(movieRepository.findByTitle(movieTitle)).thenReturn(Optional.of(new Movie()));
        when(roomRepository.findByName(roomName)).thenReturn(Optional.of(new Room()));
        when(screeningRepository.findScreeningByMovieAndRoomAndDate(any(), any(), any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingServiceImpl.showPrice(movieTitle, roomName, dateTime, seatListInString));
    }

    //@Test
    //void testShowPriceInvalidBooking() throws NotFoundException {
    //    // Arrange
    //    String movieTitle = "Test Movie";
    //    String roomName = "Room1";
    //    LocalDateTime dateTime = LocalDateTime.now();
    //    String seatListInString = "1,1 2,2 3,3";
//
    //    Movie movie = new Movie("Test Movie", "Action", 120);
    //    Room room = new Room("Room1", 10, 10);
    //    Screening screening = new Screening(movie, room, dateTime);
    //    List<Seat> seatsToBook = Arrays.asList(new Seat(1, 1), new Seat(2, 2), new Seat(3, 3));
//
    //    when(movieRepository.findByTitle(movieTitle)).thenReturn(Optional.of(movie));
    //    when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
    //    when(screeningRepository.findScreeningByMovieAndRoomAndDate(movie, room, dateTime)).thenReturn(Optional.of(screening));
    //    when(calculator.calculate(screening, seatsToBook.size())).thenReturn(30);
//
    //    Booking booking = new Booking(screening, seatsToBook, 30);
//
    //    // Mock canBooking to return false
    //    when(!bookingServiceImpl.canBooking(booking)).thenReturn(false);
//
    //    // Assert that the exception message matches your expectation
    //    assertThrows(NotFoundException.class, () -> bookingServiceImpl.showPrice(movieTitle, roomName, dateTime, seatListInString));
    //}

    @Test
    void testIsSeatExists() {
        // Arrange
        Movie movie = new Movie("Test Movie", "Action", 120);
        Room room = new Room("Test Room", 5, 5);
        Screening screening = new Screening(movie, room, LocalDateTime.now());

        Seat validSeat = new Seat(3, 3);
        Seat invalidSeat1 = new Seat(6, 3);
        Seat invalidSeat2 = new Seat(3, 6);

        Booking booking = new Booking(screening, Arrays.asList(validSeat, invalidSeat1, invalidSeat2), 30);

        when(bookingRepository.findAllByScreening_MovieAndScreening_RoomAndScreening_Date(any(), any(), any()))
                .thenReturn(Collections.emptyList());

        // Act and Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            bookingServiceImpl.canBooking(booking);
        });

        // Assert that the exception message matches your expectation
        assertEquals("Seat (6,3)(3,6) does not exist in this room", exception.getMessage());
    }

    @Test
    void testIsSeatNotBooked() throws NotFoundException {
        // Arrange
        Movie movie = new Movie("Test Movie", "Action", 120);
        Room room = new Room("Test Room", 5, 5);
        Screening screening = new Screening(movie, room, LocalDateTime.now());

        Seat bookedSeat = new Seat(3, 3);
        Seat unbookedSeat1 = new Seat(1, 1);
        Seat unbookedSeat2 = new Seat(2, 2);

        Booking booking = new Booking(screening, Arrays.asList(bookedSeat, unbookedSeat1, unbookedSeat2), 30);

        Booking otherBooking = new Booking(screening, Arrays.asList(bookedSeat), 30);

        when(bookingRepository.findAllByScreening_MovieAndScreening_RoomAndScreening_Date(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Collections.singletonList(otherBooking));

        // Act and Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            bookingServiceImpl.canBooking(booking);
        });

        // Assert that the exception message matches your expectation
        assertEquals("Seat (3,3) is already taken", exception.getMessage());
    }
}
