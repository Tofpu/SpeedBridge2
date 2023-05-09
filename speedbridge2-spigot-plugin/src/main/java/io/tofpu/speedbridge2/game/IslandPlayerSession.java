package io.tofpu.speedbridge2.game;

import com.github.tofpu.gameengine.GamePlayerSession;

import java.util.UUID;

public class IslandPlayerSession implements GamePlayerSession {

    private final UUID id;

    public IslandPlayerSession(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
