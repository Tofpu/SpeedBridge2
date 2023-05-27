package com.github.tofpu.speedbridge2.event;

import com.github.tofpu.speedbridge2.listener.Event;
import java.util.UUID;

public class MessageEvent extends Event {
    private final UUID sender;
    private final String message;

    public MessageEvent(UUID sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public UUID sender() {
        return sender;
    }

    public String message() {
        return message;
    }
}
