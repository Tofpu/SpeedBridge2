package io.github.tofpu.speedbridge.gameengine;

public abstract class BaseGameHandler<D extends GameData> {
    protected final StateManager<D> stateManager = new StateManager<>();

    protected BaseGameHandler() {
    }

    public abstract void registerStates();
}
