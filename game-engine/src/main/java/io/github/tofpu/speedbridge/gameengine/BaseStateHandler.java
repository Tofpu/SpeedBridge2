package io.github.tofpu.speedbridge.gameengine;

public abstract class BaseStateHandler<D extends GameData> {
    protected final StateManager<D> stateManager = new StateManager<>();

    public abstract void registerStates();
}
