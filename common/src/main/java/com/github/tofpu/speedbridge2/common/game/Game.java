package com.github.tofpu.speedbridge2.common.game;

import com.github.tofpu.speedbridge2.common.game.state.InitiateGameState;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

public class Game<D extends GameData> {
    private final D gameData;
    private @NotNull GameState<D> gameState = new InitiateGameState<>();

    private final Stack<GameState<D>> lastStateDispatch = new Stack<>();

    public Game(@NotNull D gameData) {
        this.gameData = gameData;
    }

    public void dispatch(@NotNull GameState<D> newState) {
        if (newState.test(this)) {
            System.out.printf("Switching to %s state from %s state%n", name(newState), name(gameState));
            lastStateDispatch.add(newState);
            newState.apply(this);
        } else {
            throw new RuntimeException(String.format("%s cannot be applied on %s state", name(newState), name(gameState)));
        }
        if (lastStateDispatch.isEmpty()) return;

        this.gameState = lastStateDispatch.pop();
        if (!lastStateDispatch.empty()) {
            lastStateDispatch.clear();
        }
    }

    private String name(GameState<D> newState) {
        return newState.getClass().getSimpleName();
    }

    public D data() {
        return gameData;
    }

    public GameState<D> state() {
        return gameState;
    }
}
