package com.epam.training.ticketservice;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class Tesztecske {
    @ShellMethod(key="helo", value = "cicavagyokkarmolok")
    public String helocicaativagyok(){
        return "miauu";
    }
}
