package com.github.tofpu.speedbridge2.common.game;

import org.jetbrains.annotations.NotNull;

public interface GameState<D extends GameData> {
    void apply(final Game<D> game);
    boolean test(final Game<D> game);

    @NotNull
    GameStateTag tag();
}
