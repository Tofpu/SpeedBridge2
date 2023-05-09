package io.tofpu.speedbridge2.game;

import com.github.tofpu.gameengine.GameSession;

import java.util.UUID;

public class IslandGameSession implements GameSession {
    private final UUID playerId;
    private final int islandSlot;

    public IslandGameSession(UUID playerId, int islandSlot) {
        this.playerId = playerId;
        this.islandSlot = islandSlot;
    }

    public int getSlot() {
        return islandSlot;
    }

    @Override
    public UUID getId() {
        return playerId;
    }
}
