package com.github.tofpu.speedbridge2.bridge.setup;

import com.github.tofpu.speedbridge2.bridge.core.Game;
import com.github.tofpu.speedbridge2.bridge.core.state.StartGameState;

class BeginSetupState extends StartGameState {
    @Override
    public void apply(Game game) {
        apply((IslandSetup) game);
    }

    public void apply(IslandSetup game) {
        SetupPlayer gamePlayer = (SetupPlayer) game.gamePlayer();
        gamePlayer.player().teleport(game.land().getIslandLocation());
    }
}