package com.github.tofpu.speedbridge2.bridge.core.state;

import com.github.tofpu.speedbridge2.bridge.core.Game;
import com.github.tofpu.speedbridge2.bridge.core.GameData;
import com.github.tofpu.speedbridge2.bridge.core.GameState;

public abstract class StopGameState<D extends GameData> implements GameState<D> {
    @Override
    public boolean test(Game<D> game) {
        return !(game.state() instanceof StopGameState);
    }
}
