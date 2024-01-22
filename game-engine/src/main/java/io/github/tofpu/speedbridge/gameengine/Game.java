package io.github.tofpu.speedbridge.gameengine;

import org.jetbrains.annotations.NotNull;

import java.util.Stack;

public class Game<D extends GameData> {
    private final D gameData;
    private final StateManager<D> stateManager;
    private @NotNull GameStateType<D> gameStateType = new GameStateType.InitiateGameStateType<>();

    private final Stack<GameStateType<D>> lastStateDispatch = new Stack<>();

    public Game(@NotNull D gameData, @NotNull StateManager<D> stateManager) {
        this.gameData = gameData;
        this.stateManager = stateManager;
    }

    public void dispatch(@NotNull GameStateType<D> newState) {
        if (newState.test(this)) {
            System.out.printf("Switching to %s state from %s state%n", name(newState), name(gameStateType));
            lastStateDispatch.add(newState);
            stateManager.callListener(newState, this);
        } else {
            throw new RuntimeException(String.format("%s cannot be applied on %s state", name(newState), name(gameStateType)));
        }
        if (lastStateDispatch.isEmpty()) return;

        this.gameStateType = lastStateDispatch.pop();
        if (!lastStateDispatch.empty()) {
            lastStateDispatch.clear();
        }
    }

    private String name(GameStateType<D> newState) {
        return newState.getClass().getSimpleName();
    }

    public D data() {
        return gameData;
    }

    public GameStateType<D> stateType() {
        return gameStateType;
    }
}
