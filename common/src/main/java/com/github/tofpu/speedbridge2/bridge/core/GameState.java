package com.github.tofpu.speedbridge2.bridge.core;

public interface GameState<D extends GameData> {
    void apply(final Game<D> game);
    boolean test(final Game<D> game);
}
