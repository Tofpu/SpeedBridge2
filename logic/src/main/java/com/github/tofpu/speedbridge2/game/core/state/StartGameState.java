package com.github.tofpu.speedbridge2.game.core.state;

import com.github.tofpu.speedbridge2.game.core.Game;

public abstract class StartGameState implements Game.GameState {
    @Override
    public boolean test(Game game) {
        return game.gameState() instanceof InitiateGameState || game.gameState() instanceof StopGameState;
    }
}
