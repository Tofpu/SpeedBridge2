package com.github.tofpu.speedbridge2.bridge.game.state.custom;

import com.github.tofpu.speedbridge2.GameAdapter;
import com.github.tofpu.speedbridge2.bridge.core.state.StartGameState;
import com.github.tofpu.speedbridge2.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.bridge.game.IslandGameHandler;
import com.github.tofpu.speedbridge2.bridge.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeGameState;

public class IslandResetGameState implements BridgeGameState {
    private final GameAdapter gameAdapter;

    IslandResetGameState(GameAdapter gameAdapter) {
        this.gameAdapter = gameAdapter;
    }

    @Override
    public void apply(IslandGameHandler handler, IslandGame game) {
        System.out.println("IslandResetGameState -- beginning");
        IslandGamePlayer player = game.player();

        System.out.println("IslandResetGameState -- resetting game");
        gameAdapter.resetGame(handler, game, player);

        System.out.println("IslandResetGameState -- dispatching GameStartedState");
        game.dispatch(handler.coreStateProvider().startedState());

        System.out.println("IslandResetGameState -- ending");
    }

    @Override
    public boolean test(IslandGame game) {
        return game.gameState() instanceof StartGameState;
    }
}
