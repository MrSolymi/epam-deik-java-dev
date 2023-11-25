import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.exceptions.AccountLeftSignedIn;
import com.epam.training.ticketservice.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.AccountType;
import com.epam.training.ticketservice.repositories.AccountRepository;
import com.epam.training.ticketservice.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountServiceImpl accountServiceImpl;

    @Test
    public void testCreateAccount() throws AlreadyExistsException {
        String username = "testUsername";
        String password = "testPassword";
        Account account = Account.builder()
                .username(username)
                .password(password)
                .accountType(AccountType.USER)
                .build();

        accountServiceImpl.signUp(username, password);

        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void testCreateAccountShouldThrowAccountAlreadyExistsException() {
        String username = "testUsername";
        String password = "testPassword";
        Account account = Account.builder()
                .username(username)
                .password(password)
                .accountType(AccountType.USER)
                .build();

        when(accountRepository.findByUsername(account.getUsername())).thenReturn(Optional.of(account));

        Assertions.assertThrows(AlreadyExistsException.class, () -> accountServiceImpl.signUp(username, password));
        verify(accountRepository, never()).save(account);
    }

    @Test
    public void testSignInWithValidCredentials() throws AccountLeftSignedIn {
        String username = "testUsername";
        String password = "testPassword";
        Account user = new Account(username, password, AccountType.USER);
        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<AccountDto> result = accountServiceImpl.signIn(username, password);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().username());
        assertEquals(AccountType.USER, result.get().accountType());
    }

    @Test
    public void testSignInPrivilegedWithValidCredentials() throws AccountLeftSignedIn {
        String username = "testUsername";
        String password = "testPassword";
        Account user = new Account(username, password, AccountType.ADMIN);
        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<AccountDto> result = accountServiceImpl.signInPrivileged(username, password);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().username());
        assertEquals(AccountType.ADMIN, result.get().accountType());
    }

    @Test
    public void testSignInWithValidCredentialsButWasAdmin() throws AccountLeftSignedIn {
        String username = "testUsername";
        String password = "testPassword";
        Account user = new Account(username, password, AccountType.ADMIN);
        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<AccountDto> result = accountServiceImpl.signIn(username, password);

        assertFalse(result.isPresent());
    }

    @Test
    public void testSignInPrivilegedWithValidCredentialsButWasNotAdmin() throws AccountLeftSignedIn {
        String username = "testUsername";
        String password = "testPassword";
        Account user = new Account(username, password, AccountType.USER);
        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<AccountDto> result = accountServiceImpl.signInPrivileged(username, password);

        assertFalse(result.isPresent());
    }

    @Test
    public void testSignInWithInvalidUsername() throws AccountLeftSignedIn {
        String username = "testUserNotExists";
        String password = "testPassword";
        when(accountRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<AccountDto> result = accountServiceImpl.signIn(username, password);

        assertFalse(result.isPresent());
    }

    @Test
    public void testSignInPrivilegedWithInvalidUsername() throws AccountLeftSignedIn {
        String username = "testUserNotExists";
        String password = "testPassword";
        when(accountRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<AccountDto> result = accountServiceImpl.signInPrivileged(username, password);

        assertFalse(result.isPresent());
    }

    @Test
    public void testSignInWithInvalidPassword() throws AccountLeftSignedIn {
        String username = "testUsername";
        String password = "wrongPassword";
        Account user = new Account(username, "correctPassword", AccountType.USER);
        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<AccountDto> result = accountServiceImpl.signIn(username, password);

        assertFalse(result.isPresent());
    }

    @Test
    public void testSignInPrivilegedWithInvalidPassword() throws AccountLeftSignedIn {
        String username = "testUsername";
        String password = "wrongPassword";
        Account user = new Account(username, "correctPassword", AccountType.ADMIN);
        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<AccountDto> result = accountServiceImpl.signInPrivileged(username, password);

        assertFalse(result.isPresent());
    }

    @Test
    public void testSignInWithAccountLeftSignedIn() {
        String username = "testUsername";
        String password = "testPassword";

        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(new Account(username, password, AccountType.USER)));

        assertThrows(AccountLeftSignedIn.class, () -> {
            accountServiceImpl.signIn(username, password);
            accountServiceImpl.signIn("anotherUser", "anotherPassword");
        });
    }

    @Test
    public void testSignInPrivilegedWithAccountLeftSignedIn() {
        String username = "testUsername";
        String password = "testPassword";

        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(new Account(username, password, AccountType.ADMIN)));

        assertThrows(AccountLeftSignedIn.class, () -> {
            accountServiceImpl.signInPrivileged(username, password);
            accountServiceImpl.signInPrivileged("anotherUser", "anotherPassword");
        });
    }

    @Test
    public void testLogOutWhenLoggedIn() throws AccountLeftSignedIn {
        String username = "testUsername";
        String password = "testPassword";
        accountServiceImpl.signIn(username, password);

        Optional<AccountDto> result = accountServiceImpl.logOut();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testLogOutWhenLoggedOut() throws AccountLeftSignedIn {
        Optional<AccountDto> result = accountServiceImpl.logOut();

        assertTrue(result.isEmpty());
    }



}

