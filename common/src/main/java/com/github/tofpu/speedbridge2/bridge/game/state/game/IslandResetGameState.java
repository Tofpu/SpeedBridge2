package com.github.tofpu.speedbridge2.bridge.game.state.game;

import com.github.tofpu.speedbridge2.PlatformGameAdapter;
import com.github.tofpu.speedbridge2.game.state.StartGameState;
import com.github.tofpu.speedbridge2.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.bridge.game.IslandGameData;
import com.github.tofpu.speedbridge2.bridge.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.bridge.game.state.GameStateHandler;
import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeGameState;
import com.github.tofpu.speedbridge2.game.Game;

public class IslandResetGameState implements BridgeGameState {
    private final GameStateHandler stateHandler;
    private final PlatformGameAdapter gameAdapter;

    IslandResetGameState(GameStateHandler stateHandler, PlatformGameAdapter gameAdapter) {
        this.stateHandler = stateHandler;
        this.gameAdapter = gameAdapter;
    }

    @Override
    public void apply(Game<IslandGameData> game) {
        System.out.println("IslandResetGameState -- beginning");
        IslandGamePlayer player = game.data().gamePlayer();

        System.out.println("IslandResetGameState -- resetting game");
        gameAdapter.resetGame((IslandGame) game, player);

        System.out.println("IslandResetGameState -- dispatching GameStartedState");
        stateHandler.triggerStartedState(game);

        System.out.println("IslandResetGameState -- ending");
    }

    @Override
    public boolean test(Game<IslandGameData> game) {
        return game.state() instanceof StartGameState;
    }
}
