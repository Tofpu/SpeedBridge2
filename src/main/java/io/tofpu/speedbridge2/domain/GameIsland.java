package io.tofpu.speedbridge2.domain;

public final class GameIsland {
    private final int slot;
    private final GamePlayer gamePlayer;

    public GameIsland(final int slot, final GamePlayer gamePlayer) {
        this.slot = slot;
        this.gamePlayer = gamePlayer;
    }

    public int getSlot() {
        return slot;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }
}
