package com.github.tofpu.speedbridge2.bridge.core.state;

import com.github.tofpu.speedbridge2.bridge.core.Game;

public abstract class StopGameState implements Game.GameState {
    @Override
    public boolean test(Game game) {
        return !(game.gameState() instanceof StopGameState);
    }
}
