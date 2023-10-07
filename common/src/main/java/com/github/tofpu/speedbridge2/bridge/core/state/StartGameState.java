package com.github.tofpu.speedbridge2.bridge.core.state;

import com.github.tofpu.speedbridge2.bridge.core.Game;
import com.github.tofpu.speedbridge2.bridge.core.GameHandler;

public abstract class StartGameState<H extends GameHandler<?, H, ?>, G extends Game<H, ?>> implements Game.GameState<H, G> {
    @Override
    public boolean test(G game) {
        return !(game.gameState() instanceof StopGameState);
    }
}
