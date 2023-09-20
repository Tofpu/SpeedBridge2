package com.github.tofpu.speedbridge2.game.core;

import com.github.tofpu.speedbridge2.game.core.state.InitiateGameState;

import java.util.Stack;

public class Game {
    private final GamePlayer gamePlayer;
    private final Stack<GameState> lastStateDispatch = new Stack<>();

    private GameState gameState = new InitiateGameState();

    public Game(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public <S extends GameState> void dispatch(GameState newState) {
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

    private static String name(GameState newState) {
        return newState.getClass().getSimpleName();
    }

    public interface GameState {
        void apply(final Game game);
        boolean test(final Game game);
    }

    public GameState gameState() {
        return gameState;
    }

    public GamePlayer gamePlayer() {
        return gamePlayer;
    }
}
