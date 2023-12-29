package com.github.tofpu.speedbridge2.game.state;

import com.github.tofpu.speedbridge2.game.Game;
import com.github.tofpu.speedbridge2.game.GameData;
import com.github.tofpu.speedbridge2.game.GameState;

public abstract class StopGameState<D extends GameData> implements GameState<D> {
    @Override
    public boolean test(Game<D> game) {
        return !(game.state() instanceof StopGameState);
    }
}
