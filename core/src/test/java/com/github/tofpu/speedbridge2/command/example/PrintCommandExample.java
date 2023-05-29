package com.github.tofpu.speedbridge2.command.example;

import com.github.tofpu.speedbridge2.command.annontation.Command;
import com.github.tofpu.speedbridge2.command.annontation.Default;
import com.github.tofpu.speedbridge2.command.annontation.Subcommand;

@Command(name = "print")
public class PrintCommandExample {
    private String defaultPrintMessage = "Default print message!";

    @Default
    public void print(String input) {
        if (input == null) {
            System.out.println(defaultPrintMessage);
            return;
        }
        System.out.println(input);
    }

    // parent print set hi
    @Subcommand(name = "set")
    public void setDefaultPrintMessage(String message) {
        this.defaultPrintMessage = message;
    }

    @Subcommand(name = "test message")
    public void testHi() {
        System.out.println("hi");
    }
}
