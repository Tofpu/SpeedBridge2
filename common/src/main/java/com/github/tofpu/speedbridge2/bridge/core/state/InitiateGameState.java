package com.github.tofpu.speedbridge2.bridge.core.state;

import com.github.tofpu.speedbridge2.bridge.core.Game;
import com.github.tofpu.speedbridge2.bridge.core.GameData;
import com.github.tofpu.speedbridge2.bridge.core.GameState;

@SuppressWarnings("all")
public class InitiateGameState<D extends GameData> implements GameState<D> {
    @Override
    public void apply(Game<D> game) {

    }

    @Override
    public boolean test(Game<D> game) {
        return true;
    }
}
