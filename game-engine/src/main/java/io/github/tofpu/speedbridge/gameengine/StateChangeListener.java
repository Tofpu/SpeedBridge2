package io.github.tofpu.speedbridge.gameengine;

public interface StateChangeListener<D extends GameData> {
    void onGameStateChange(Game<D> game, GameStateType<D> stateChange);
}
