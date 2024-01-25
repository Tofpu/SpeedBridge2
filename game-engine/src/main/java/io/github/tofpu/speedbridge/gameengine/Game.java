package io.github.tofpu.speedbridge.gameengine;

import org.jetbrains.annotations.NotNull;


public class Game<D extends GameData> {
    private final D gameData;
    private final StateManager<D> stateManager;
    private @NotNull GameStateType<D> gameStateType = new GameStateType.InitiateGameStateType<>();

    public Game(@NotNull D gameData, @NotNull StateManager<D> stateManager) {
        this.gameData = gameData;
        this.stateManager = stateManager;
    }

    public void dispatch(@NotNull GameStateType<D> newState) {
        if (newState.test(this)) {
            System.out.printf("Switching to %s state from %s state%n", name(newState), name(gameStateType));
            GameStateType<D> prevState = gameStateType;
            gameStateType = newState;
            try {
                stateManager.callListener(newState, prevState, this);
            } catch (Throwable throwable) {
                // reset back to previous state if the call fails for whatever reason
                gameStateType = prevState;
                throw throwable;
            }
        } else {
            throw new RuntimeException(String.format("%s cannot be applied on %s state", name(newState), name(gameStateType)));
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
