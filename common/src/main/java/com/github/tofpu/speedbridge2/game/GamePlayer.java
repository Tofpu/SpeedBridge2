package com.github.tofpu.speedbridge2.game;

import java.util.UUID;

public abstract class GamePlayer {
    private final UUID id;

    public GamePlayer(UUID id) {
        this.id = id;
    }

    public UUID id() {
        return id;
    }
}
