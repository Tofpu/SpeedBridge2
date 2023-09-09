package com.github.tofpu.speedbridge2.game.core.state;

import com.github.tofpu.speedbridge2.game.core.Game;

public class InitiateGameState implements Game.GameState {
    @Override
    public void apply(Game game) {

    }

    @Override
    public boolean test(Game game) {
        return true;
    }
}
