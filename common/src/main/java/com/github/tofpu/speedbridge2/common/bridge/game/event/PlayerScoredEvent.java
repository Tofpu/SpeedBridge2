package com.github.tofpu.speedbridge2.common.bridge.game.event;

import com.github.tofpu.speedbridge2.common.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.event.Cancellable;
import com.github.tofpu.speedbridge2.event.Event;

public class PlayerScoredEvent extends Event implements Cancellable {
    private final IslandGamePlayer player;
    private final IslandGame islandGame;
    private final double scoreInSeconds;

    private String scoreMessage = "Scored %s seconds!";
    private boolean cancelled = false;

    public PlayerScoredEvent(IslandGamePlayer player, IslandGame islandGame, double scoreInSeconds) {
        this.player = player;
        this.islandGame = islandGame;
        this.scoreInSeconds = scoreInSeconds;
    }

    public void scoreMessage(String scoreMessage) {
        this.scoreMessage = scoreMessage;
    }

    public String scoreMessage() {
        return scoreMessage;
    }

    public IslandGamePlayer player() {
        return player;
    }

    public IslandGame islandGame() {
        return islandGame;
    }

    public double scoreInSeconds() {
        return scoreInSeconds;
    }

    @Override
    public void cancel(boolean state) {
        this.cancelled = state;
    }

    @Override
    public boolean cancelled() {
        return cancelled;
    }
}
