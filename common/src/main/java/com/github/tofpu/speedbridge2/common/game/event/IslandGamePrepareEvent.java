package com.github.tofpu.speedbridge2.common.game.event;

import com.github.tofpu.speedbridge2.common.game.IslandGame;
import com.github.tofpu.speedbridge2.common.game.IslandGamePlayer;

public class IslandGamePrepareEvent extends IslandGameEvent {
    public IslandGamePrepareEvent(IslandGame game, IslandGamePlayer player) {
        super(game, player);
    }
}
