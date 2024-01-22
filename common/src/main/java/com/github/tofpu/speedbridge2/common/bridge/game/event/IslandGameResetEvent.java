package com.github.tofpu.speedbridge2.common.bridge.game.event;

import com.github.tofpu.speedbridge2.common.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGamePlayer;

public class IslandGameResetEvent extends IslandGameEvent {
    public IslandGameResetEvent(IslandGame game, IslandGamePlayer player) {
        super(game, player);
    }
}