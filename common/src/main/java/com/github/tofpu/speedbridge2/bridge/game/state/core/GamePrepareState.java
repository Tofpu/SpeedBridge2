package com.github.tofpu.speedbridge2.bridge.game.state.core;

import com.github.tofpu.speedbridge2.GameAdapter;
import com.github.tofpu.speedbridge2.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.bridge.game.IslandGameHandler;
import com.github.tofpu.speedbridge2.bridge.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.bridge.game.state.custom.IslandResetGameState;
import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeGameState;

class GamePrepareState implements BridgeGameState {
    private final GameAdapter gameAdapter;

    public GamePrepareState(GameAdapter gameAdapter) {
        this.gameAdapter = gameAdapter;
    }

    @Override
    public void apply(IslandGameHandler handler, IslandGame game) {
        IslandGamePlayer player = game.player();
        gameAdapter.prepareGame(handler, game, player);

        game.dispatch(new GameStartedState());
    }

    @Override
    public boolean test(IslandGame game) {
        return !(game.gameState() instanceof IslandResetGameState);
    }
}
