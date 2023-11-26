import com.epam.training.ticketservice.commands.AccountCommands;
import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.dto.BookingDto;
import com.epam.training.ticketservice.dto.ScreeningDto;
import com.epam.training.ticketservice.exceptions.AccountLeftSignedIn;
import com.epam.training.ticketservice.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.exceptions.NotFoundException;
import com.epam.training.ticketservice.model.*;
import com.epam.training.ticketservice.repositories.AccountRepository;
import com.epam.training.ticketservice.services.AccountService;
import com.epam.training.ticketservice.services.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountCommandsTest {
    @Mock
    private AccountService accountService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private AccountCommands accountCommands;

    @Test
    public void testSignInPrivileged_Success() throws AccountLeftSignedIn {
        AccountDto accountDto = new AccountDto("username", AccountType.ADMIN);
        when(accountService.signInPrivileged("username", "password")).thenReturn(Optional.of(accountDto));

        String result = accountCommands.signInPrivileged("username", "password");

        assertEquals("username is successfully logged in", result);
    }

    @Test
    public void testSignInPrivileged_Failed() throws AccountLeftSignedIn {
        when(accountService.signInPrivileged("username", "password")).thenReturn(Optional.empty());

        String result = accountCommands.signInPrivileged("username", "password");

        assertEquals("Login failed due to incorrect credentials", result);
    }

    @Test
    public void testSignInPrivileged_ThrowsError() throws AccountLeftSignedIn {
        AccountDto accountDto = new AccountDto("username", AccountType.ADMIN);

        when(accountService.signInPrivileged(accountDto.username(), "password")).thenThrow(new AccountLeftSignedIn("Sign out first to sign into an another account"));

        String result = accountCommands.signInPrivileged(accountDto.username(), "password");

        assertEquals("Sign out first to sign into an another account", result);
    }

    @Test
    public void testSignIn_Success() throws AccountLeftSignedIn {
        AccountDto accountDto = new AccountDto("username", AccountType.USER);
        when(accountService.signIn("username", "password")).thenReturn(Optional.of(accountDto));

        String result = accountCommands.signIn("username", "password");

        assertEquals("username is successfully logged in", result);
    }

    @Test
    public void testSignIn_Failed() throws AccountLeftSignedIn {
        when(accountService.signIn("username", "password")).thenReturn(Optional.empty());

        String result = accountCommands.signIn("username", "password");

        assertEquals("Login failed due to incorrect credentials", result);
    }

    @Test
    public void testSignIn_ThrowsError() throws AccountLeftSignedIn {
        AccountDto accountDto = new AccountDto("username", AccountType.USER);

        when(accountService.signIn(accountDto.username(), "password")).thenThrow(new AccountLeftSignedIn("Sign out first to sign into an another account"));

        String result = accountCommands.signIn(accountDto.username(), "password");

        assertEquals("Sign out first to sign into an another account", result);
    }

    @Test
    public void testSignOut_Failed() {
        when(accountService.logOut()).thenReturn(Optional.empty());

        String result = accountCommands.signOut();

        assertEquals("You need to login first", result);
    }

    @Test
    public void testSignOutAdmin_Success() {
        AccountDto accountDto = new AccountDto("username", AccountType.ADMIN);
        when(accountService.logOut()).thenReturn(Optional.of(accountDto));

        String result = accountCommands.signOut();

        assertEquals("username is logged out", result);
    }

    @Test
    public void testSignOutUser_Success() {
        AccountDto accountDto = new AccountDto("username", AccountType.USER);
        when(accountService.logOut()).thenReturn(Optional.of(accountDto));

        String result = accountCommands.signOut();

        assertEquals("username is logged out", result);
    }

    @Test
    public void testDescribeAccount_AdminAccount() throws NotFoundException {
        AccountDto adminAccount = new AccountDto("admin", AccountType.ADMIN);
        when(accountService.describe()).thenReturn(Optional.of(adminAccount));

        String result = accountCommands.describeAccount();

        assertEquals("Signed in with privileged account 'admin'", result);
    }

    @Test
    public void testDescribeAccount_UserAccount_NoBookings() throws NotFoundException {
        AccountDto userAccount = new AccountDto("user", AccountType.USER);
        when(accountService.describe()).thenReturn(Optional.of(userAccount));
        when(bookingService.getBookingList(userAccount)).thenReturn(new ArrayList<>());

        String result = accountCommands.describeAccount();

        assertEquals("Signed in with account 'user'\nYou have not booked any tickets yet", result);
    }

    @Test
    public void testDescribeAccount_UserAccount_WithBookings() throws NotFoundException {
        AccountDto userAccount = new AccountDto("user", AccountType.USER);
        Movie movie = new Movie("movie", "action", 120);
        Room room = new Room("room", 10, 10);
        Screening screening = new Screening(movie, room, LocalDateTime.parse("2000-12-20 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        when(accountService.describe()).thenReturn(Optional.of(userAccount));

        List<BookingDto> bookingList = new ArrayList<>();
        bookingList.add(new BookingDto(userAccount, new ScreeningDto(screening), List.of(new Seat(1,2)), 1500));
        when(bookingService.getBookingList(userAccount)).thenReturn(bookingList);

        String result = accountCommands.describeAccount();

        StringBuilder sb = new StringBuilder();
        sb.append("Signed in with account 'user'").append("\n").append("Your previous bookings are").append("\n");

        for (var item : bookingList) {
            sb.append(item).append("\n");
        }
        sb.delete(sb.length() - 1, sb.length());

        assertEquals(sb.toString(), result);
    }

    @Test
    public void testDescribeAccount_NotSignedIn() throws NotFoundException {
        when(accountService.describe()).thenReturn(Optional.empty());

        String result = accountCommands.describeAccount();

        assertEquals("You are not signed in", result);
    }

    @Test
    public void testDescribeAccount_UserAccount_WithMoreBookings() throws NotFoundException {
        AccountDto userAccount = new AccountDto("user", AccountType.USER);
        Movie movie1 = new Movie("movie1", "action", 120);
        Movie movie2 = new Movie("movie2", "comedy", 90);
        Room room1 = new Room("room1", 10, 10);
        Room room2 = new Room("room2", 8, 8);
        Screening screening1 = new Screening(movie1, room1, LocalDateTime.parse("2000-12-20 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Screening screening2 = new Screening(movie2, room2, LocalDateTime.parse("2000-12-21 15:30", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        when(accountService.describe()).thenReturn(Optional.of(userAccount));

        List<BookingDto> bookingList = new ArrayList<>();
        bookingList.add(new BookingDto(userAccount, new ScreeningDto(screening1), List.of(new Seat(1,2)), 1500));
        bookingList.add(new BookingDto(userAccount, new ScreeningDto(screening2), List.of(new Seat(3,4)), 2000));
        when(bookingService.getBookingList(userAccount)).thenReturn(bookingList);

        String result = accountCommands.describeAccount();

        StringBuilder sb = new StringBuilder();
        sb.append("Signed in with account 'user'").append("\n").append("Your previous bookings are").append("\n");
        for (var item : bookingList) {
            sb.append(item).append("\n");
        }
        sb.delete(sb.length() - 1, sb.length());

        assertEquals(sb.toString(), result);
    }
    @Test
    public void testSignUp_SuccessfulRegistration() throws AlreadyExistsException {
        String result = accountCommands.signUp("newUser", "password");

        assertEquals("Registration was successful", result);
        verify(accountService, times(1)).signUp("newUser", "password");
    }

    @Test
    public void testSignUp_FailedRegistration_AlreadyExists() throws AlreadyExistsException {
        doThrow(new AlreadyExistsException("Username already exists")).when(accountService).signUp("existingUser", "password");

        String result = accountCommands.signUp("existingUser", "password");

        assertEquals("Registration failed! Username already exists", result);
        verify(accountService, times(1)).signUp("existingUser", "password");
    }

}
