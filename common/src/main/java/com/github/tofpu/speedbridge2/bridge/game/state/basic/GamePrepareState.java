package com.github.tofpu.speedbridge2.bridge.game.state.basic;

import com.github.tofpu.speedbridge2.PlatformGameAdapter;
import com.github.tofpu.speedbridge2.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.bridge.game.IslandGameData;
import com.github.tofpu.speedbridge2.bridge.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.bridge.game.state.game.IslandResetGameState;
import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeGameState;
import com.github.tofpu.speedbridge2.game.Game;

class GamePrepareState implements BridgeGameState {
    private final PlatformGameAdapter gameAdapter;

    public GamePrepareState(PlatformGameAdapter gameAdapter) {
        this.gameAdapter = gameAdapter;
    }

    @Override
    public void apply(Game<IslandGameData> game) {
        IslandGamePlayer player = game.data().gamePlayer();
        gameAdapter.prepareGame((IslandGame) game, player);
        game.dispatch(new GameStartedState());
    }

    @Override
    public boolean test(Game<IslandGameData> game) {
        return !(game.state() instanceof IslandResetGameState);
    }
}
