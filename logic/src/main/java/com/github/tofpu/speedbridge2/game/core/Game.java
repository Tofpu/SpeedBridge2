package com.github.tofpu.speedbridge2.game.core;

import com.github.tofpu.speedbridge2.game.core.state.InitiateGameState;

public class Game {
    private final GamePlayer gamePlayer;
    private GameState gameState = new InitiateGameState();

    public Game(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public <S extends GameState> void dispatch(GameState newState) {
        if (newState.test(this)) {
            newState.apply(this);
        } else {
            throw new RuntimeException("Improper state: " + newState);
        }
        this.gameState = newState;
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
