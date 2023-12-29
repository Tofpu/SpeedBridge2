package com.github.tofpu.speedbridge2.game.state;

import com.github.tofpu.speedbridge2.game.Game;
import com.github.tofpu.speedbridge2.game.GameData;
import com.github.tofpu.speedbridge2.game.GameState;

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
