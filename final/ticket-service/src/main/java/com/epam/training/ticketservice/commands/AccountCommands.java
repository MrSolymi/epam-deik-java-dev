package com.epam.training.ticketservice.commands;

import jdk.dynalink.beans.StaticClass;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class AccountCommands {
    @ShellMethod(key = "sign in privileged", value = "Sign in privileged with <username> <password>")
    public String signInPrivileged(String userName, String password){
        return null;
    }

    @ShellMethod(key = "sign in", value = "Sign in with <username> <password>")
    public String signIn(String userName, String password){
        return null;
    }

    @ShellMethod(key = "sign out", value = "Sign out")
    public void signOut(){

    }

    @ShellMethod(key = "describe account", value = "describe account")
    public String describeAccount(){
        return "You are not signed in";
    }
}
