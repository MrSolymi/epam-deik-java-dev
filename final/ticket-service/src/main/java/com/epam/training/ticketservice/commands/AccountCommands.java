package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.model.AccountType;
import com.epam.training.ticketservice.services.AccService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class AccountCommands {

    private final AccService accService;
    @ShellMethod(key = "sign in privileged", value = "Sign in privileged with <username> <password>")
    public String signInPrivileged(String userName, String password){
        return accService.signInPrivileged(userName, password)
                .map(accountDto -> accountDto.username() + " is successfully logged in!")
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "sign in", value = "Sign in with <username> <password>")
    public String signIn(String userName, String password){
        return accService.signIn(userName, password)
                .map(accountDto -> accountDto.username() + " is successfully logged in!")
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "sign out", value = "Sign out")
    public void signOut(){
        accService.logOut()
                .map(accountDto -> accountDto.username() + " is logged out!")
                .orElse("You need to login first!");
    }

    @ShellMethod(key = "describe account", value = "describe account")
    public String describeAccount(){
        Optional<AccountDto> acc = accService.describe();
        if (acc.isPresent() && acc.get().accountType() == AccountType.ADMIN){
            return "Signed in with privileged account '" + acc.get().username() + "'";
        }
        else if (acc.isPresent() && acc.get().accountType() == AccountType.USER){
            return "Signed in with account '" +acc.get().username() + "'";
        }
        else
        {
            return "You are not signed in";
        }
    }

    @ShellMethod(key = "sign up", value = "Sign up with <username> <password>")
    public String SignUp(String userName, String password) {
        try {
            accService.signUp(userName, password);
            return "Registration was successful!";
        } catch (Exception e) {
            return "Registration failed!";
        }
    }
}
