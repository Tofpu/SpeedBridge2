package com.github.tofpu.speedbridge2.common.bridge.game.event;

import com.github.tofpu.speedbridge2.common.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.event.Cancellable;
import com.github.tofpu.speedbridge2.event.Event;

public class PlayerScoredEvent extends IslandGameEvent {
    private String scoreMessage = "Scored %s seconds!";

    public PlayerScoredEvent(IslandGame game, IslandGamePlayer player) {
        super(game, player);
    }

    public void scoreMessage(String scoreMessage) {
        this.scoreMessage = scoreMessage;
    }

    public String scoreMessage() {
        return scoreMessage;
    }
}
