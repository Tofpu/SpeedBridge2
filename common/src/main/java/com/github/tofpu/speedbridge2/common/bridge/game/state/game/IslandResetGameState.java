package com.github.tofpu.speedbridge2.common.bridge.game.state.game;

import com.github.tofpu.speedbridge2.common.PlatformGameAdapter;
import com.github.tofpu.speedbridge2.common.bridge.game.state.BridgeGameStateTag;
import com.github.tofpu.speedbridge2.common.game.GameStateTag;
import com.github.tofpu.speedbridge2.common.game.state.BasicGameStateTag;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGameData;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.common.bridge.game.state.GameStateHandler;
import com.github.tofpu.speedbridge2.common.bridge.game.state.generic.BridgeGameState;
import com.github.tofpu.speedbridge2.common.game.Game;
import org.jetbrains.annotations.NotNull;

class IslandResetGameState implements BridgeGameState {
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
        return game.state().tag() == BasicGameStateTag.STARTED;
    }

    @Override
    public @NotNull GameStateTag tag() {
        return BridgeGameStateTag.RESET;
    }
}
