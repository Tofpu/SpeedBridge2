package com.github.tofpu.speedbridge2.common.bridge.game.state;

import com.github.tofpu.speedbridge2.common.bridge.game.IslandGameData;
import com.github.tofpu.speedbridge2.common.bridge.game.state.basic.BridgeBasicStateProvider;
import com.github.tofpu.speedbridge2.common.bridge.game.state.game.BridgeGameStateProvider;
import com.github.tofpu.speedbridge2.common.game.Game;
import org.jetbrains.annotations.NotNull;

public class GameStateHandler {
    private final BasicStateProvider basicStateProvider;
    private final GameStateProvider gameStateProvider;

    public GameStateHandler(BridgeBasicStateProvider basicStateProvider, BridgeGameStateProvider.Builder gameStateSupplier) {
        this.basicStateProvider = basicStateProvider;
        this.gameStateProvider = gameStateSupplier.build(this);
    }

    public void triggerResetState(@NotNull Game<IslandGameData> game) {
        game.dispatch(gameStateProvider.resetState());
    }

    public void triggerScoreState(@NotNull Game<IslandGameData> game) {
        game.dispatch(gameStateProvider.scoreState());
    }

    public void triggerStartedState(Game<IslandGameData> game) {
        game.dispatch(basicStateProvider.startedState());
    }
}
