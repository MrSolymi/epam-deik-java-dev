package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.dto.BookingDto;
import com.epam.training.ticketservice.model.AccountType;
import com.epam.training.ticketservice.services.AccountService;
import com.epam.training.ticketservice.services.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class AccountCommands {

    private final AccountService accountService;
    private final BookingService bookingService;
    @ShellMethod(key = "sign in privileged", value = "Sign in privileged with <username> <password>")
    public String signInPrivileged(String userName, String password){
        return accountService.signInPrivileged(userName, password)
                .map(accountDto -> accountDto.username() + " is successfully logged in!")
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "sign in", value = "Sign in with <username> <password>")
    public String signIn(String userName, String password){
        return accountService.signIn(userName, password)
                .map(accountDto -> accountDto.username() + " is successfully logged in!")
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "sign out", value = "Sign out")
    public void signOut(){
        accountService.logOut()
                .map(accountDto -> accountDto.username() + " is logged out!")
                .orElse("You need to login first!");
    }

    @ShellMethod(key = "describe account", value = "describe account")
    public String describeAccount(){
        Optional<AccountDto> acc = accountService.describe();
        if (acc.isPresent() && acc.get().accountType() == AccountType.ADMIN){
            return "Signed in with privileged account '" + acc.get().username() + "'";
        }
        else if (acc.isPresent() && acc.get().accountType() == AccountType.USER){
            StringBuilder sb = new StringBuilder();
            sb.append("Signed in with account '" +acc.get().username() + "'").append("\n");
            List<BookingDto> bookingList = bookingService.getBookingsList();
            if (bookingList.isEmpty())
                sb.append("You have not booked any tickets yet");
            else {
                for (var item : bookingList){
                    sb.append(item).append("\n");
                }
                sb.delete(sb.length()-1, sb.length());
            }
            return sb.toString();
        }
        else
        {
            return "You are not signed in";
        }
    }

    @ShellMethod(key = "sign up", value = "Sign up with <username> <password>")
    public String SignUp(String userName, String password) {
        try {
            accountService.signUp(userName, password);
            return "Registration was successful!";
        } catch (Exception e) {
            return "Registration failed!";
        }
    }

}
