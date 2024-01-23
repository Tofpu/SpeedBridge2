package com.github.tofpu.speedbridge2.common.game.event;

import com.github.tofpu.speedbridge2.common.game.IslandGame;
import com.github.tofpu.speedbridge2.common.game.IslandGamePlayer;

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
