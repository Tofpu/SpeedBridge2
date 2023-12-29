package com.github.tofpu.speedbridge2.common.game.state;

import com.github.tofpu.speedbridge2.common.game.Game;
import com.github.tofpu.speedbridge2.common.game.GameData;
import com.github.tofpu.speedbridge2.common.game.GameState;
import com.github.tofpu.speedbridge2.common.game.GameStateTag;
import org.jetbrains.annotations.NotNull;

public abstract class StopGameState<D extends GameData> implements GameState<D> {
    @Override
    public boolean test(Game<D> game) {
        return !(game.state() instanceof StopGameState);
    }

    @Override
    public @NotNull GameStateTag tag() {
        return BasicGameStateTag.STOPPED;
    }
}
