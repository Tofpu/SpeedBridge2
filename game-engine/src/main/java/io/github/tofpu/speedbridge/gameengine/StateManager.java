package io.github.tofpu.speedbridge.gameengine;

import java.util.HashMap;
import java.util.Map;

public class StateManager<D extends GameData> {
    private final Map<GameStateType<D>, StateChangeListener<D>> stateListenerMap = new HashMap<>();

    public void addState(GameStateType<D> state, StateChangeListener<D> stateChangeListener) {
        stateListenerMap.put(state, stateChangeListener);
    }

    public void callListener(GameStateType<D> newState, GameStateType<D> prevState, Game<D> game) {
        StateChangeListener<D> listener = stateListenerMap.get(newState);
        if (listener != null) {
            listener.onGameStateChange(game, prevState, newState);
        }
    }
}
