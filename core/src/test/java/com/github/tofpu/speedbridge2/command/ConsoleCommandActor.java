package com.github.tofpu.speedbridge2.command;

public class ConsoleCommandActor implements CommandActor {
    @Override
    public void sendMessage(String message) {
        System.out.println("Message received: " + message);
    }
}
