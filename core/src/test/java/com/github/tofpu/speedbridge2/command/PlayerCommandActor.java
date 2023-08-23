package com.github.tofpu.speedbridge2.command;

import java.util.UUID;

public class PlayerCommandActor implements CommandActor {
    private final UUID id;

    public PlayerCommandActor(UUID id) {
        this.id = id;
    }

    @Override
    public void sendMessage(String message) {
        System.out.println("Message received: " + message);
    }

    public UUID id() {
        return id;
    }
}
