package com.github.tofpu.speedbridge2.bridge.game.state.basic;

import com.github.tofpu.speedbridge2.PlatformGameAdapter;
import com.github.tofpu.speedbridge2.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.bridge.game.IslandGameData;
import com.github.tofpu.speedbridge2.bridge.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.bridge.game.state.BridgeGameStateTag;
import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeGameState;
import com.github.tofpu.speedbridge2.game.Game;
import com.github.tofpu.speedbridge2.game.GameStateTag;
import com.github.tofpu.speedbridge2.game.state.BasicGameStateTag;
import org.jetbrains.annotations.NotNull;

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
        return game.state().tag() != BridgeGameStateTag.RESET;
    }

    @Override
    public @NotNull GameStateTag tag() {
        return BasicGameStateTag.PREPARE;
    }
}
