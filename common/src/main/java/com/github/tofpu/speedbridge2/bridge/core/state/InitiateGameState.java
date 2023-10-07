package com.github.tofpu.speedbridge2.bridge.core.state;

import com.github.tofpu.speedbridge2.bridge.core.Game;
import com.github.tofpu.speedbridge2.bridge.core.GameHandler;

@SuppressWarnings("all")
public class InitiateGameState implements Game.GameState {
    @Override
    public void apply(GameHandler handler, Game game) {

    }

    @Override
    public boolean test(Game game) {
        return true;
    }
}
