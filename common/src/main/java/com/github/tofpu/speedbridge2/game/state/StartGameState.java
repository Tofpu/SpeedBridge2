package com.github.tofpu.speedbridge2.game.state;

import com.github.tofpu.speedbridge2.game.Game;
import com.github.tofpu.speedbridge2.game.GameData;
import com.github.tofpu.speedbridge2.game.GameState;
import com.github.tofpu.speedbridge2.game.GameStateTag;
import org.jetbrains.annotations.NotNull;

public abstract class StartGameState<D extends GameData> implements GameState<D> {
    @Override
    public boolean test(Game<D> game) {
        return !(game.state() instanceof StopGameState);
    }

    @Override
    public @NotNull GameStateTag tag() {
        return BasicGameStateTag.STARTED;
    }
}
