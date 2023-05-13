package com.github.tofpu.speedbridge2.listener;

import java.util.UUID;

public class MessageEvent extends Event {

    private final UUID userId;
    private final String message;

    public MessageEvent(UUID userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }
}
