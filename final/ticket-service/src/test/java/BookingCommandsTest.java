import com.epam.training.ticketservice.commands.BookingCommands;
import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.dto.BookingDto;
import com.epam.training.ticketservice.dto.ScreeningDto;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.exceptions.SeatAlreadyTakenException;
import com.epam.training.ticketservice.model.*;
import com.epam.training.ticketservice.services.AccountService;
import com.epam.training.ticketservice.services.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingCommandsTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private BookingCommands bookingCommands;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Test
    public void testCreateBooking_SuccessfulBooking() throws NotFoundException, SeatAlreadyTakenException {
        String movieTitle = "Movie 1";
        String roomName = "Room 1";
        String startTime = "2023-12-01 18:00";
        String seatListString = "1,2 3,4";

        AccountDto accountDto = new AccountDto("user", AccountType.USER);
        when(accountService.describe()).thenReturn(Optional.of(accountDto));

        Movie movie = new Movie(movieTitle, "action", 120);
        Room room = new Room(roomName, 10, 10);
        Screening screening = new Screening(movie, room, LocalDateTime.parse(startTime, formatter));

        BookingDto bookingDto = new BookingDto(accountDto, new ScreeningDto(screening), List.of(new Seat(5, 5)), 1500);
        when(bookingService.getBookingList(accountDto)).thenReturn(List.of(bookingDto));

        String result = bookingCommands.createBooking(movieTitle, roomName, startTime, seatListString);

        verify(bookingService, times(1)).createBooking(movieTitle, roomName, LocalDateTime.parse(startTime, formatter), seatListString, accountDto);
        assertEquals(bookingDto.toStringBooked(), result);
    }

    @Test
    public void testCreateBooking_BookingDtoIsEmpty() throws NotFoundException{
        String movieTitle = "Movie 1";
        String roomName = "Room 1";
        String startTime = "2023-12-01 18:00";
        String seatListString = "1,2 3,4";

        when(accountService.describe()).thenReturn(Optional.empty());

        String result = bookingCommands.createBooking(movieTitle, roomName, startTime, seatListString);

        assertEquals("You need to sign in first", result);
    }

    @Test
    public void testCreateBooking_FailedBooking() throws NotFoundException, SeatAlreadyTakenException {
        String movieTitle = "Movie 1";
        String roomName = "Room 1";
        String startTime = "2023-12-01 18:00";
        String seatListString = "1,2 3,4";

        AccountDto accountDto = new AccountDto("user", AccountType.USER);
        when(accountService.describe()).thenReturn(Optional.of(accountDto));

        doThrow(new RuntimeException("Booking failed")).when(bookingService).createBooking(any(), any(), any(), any(), any());

        String result = bookingCommands.createBooking(movieTitle, roomName, startTime, seatListString);

        assertEquals("Booking failed", result);
        verify(bookingService, times(1)).createBooking(movieTitle, roomName, LocalDateTime.parse(startTime, formatter), seatListString, accountDto);
    }


}
