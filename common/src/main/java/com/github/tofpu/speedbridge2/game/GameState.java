package com.github.tofpu.speedbridge2.game;

public interface GameState<D extends GameData> {
    void apply(final Game<D> game);
    boolean test(final Game<D> game);
}
