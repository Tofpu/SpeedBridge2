package com.github.tofpu.speedbridge2.bridge.core;

import com.github.tofpu.speedbridge2.bridge.core.state.InitiateGameState;

import java.util.Stack;

public class Game<H extends GameHandler<?, ?, ?>, G extends Game<H, G>> {
    private final H handler;
    private final GamePlayer gamePlayer;
    private final Stack<GameState<H, G>> lastStateDispatch = new Stack<>();

    private GameState<H, G> gameState = new InitiateGameState();

    public Game(H handler, GamePlayer gamePlayer) {
        this.handler = handler;
        this.gamePlayer = gamePlayer;
    }

    public void dispatch(GameState<H, G> newState) {
        if (newState.test((G) this)) {
            System.out.printf("Switching to %s state from %s state%n", name(newState), name(gameState));
            lastStateDispatch.add(newState);
            newState.apply(handler, (G) this);
        } else {
            throw new RuntimeException(String.format("%s cannot be applied on %s state", name(newState), name(gameState)));
        }
        if (lastStateDispatch.isEmpty()) return;

        this.gameState = lastStateDispatch.pop();
        if (!lastStateDispatch.empty()) {
            lastStateDispatch.clear();
        }
    }

    private static String name(GameState<?, ?> newState) {
        return newState.getClass().getSimpleName();
    }

    public interface GameState<H extends GameHandler<?, ?, ?>, G extends Game<H, ?>> {
        void apply(final H handler, final G game);
        boolean test(final G game);
    }

    public GameState<H, G> gameState() {
        return gameState;
    }

    public GamePlayer gamePlayer() {
        return gamePlayer;
    }
}
