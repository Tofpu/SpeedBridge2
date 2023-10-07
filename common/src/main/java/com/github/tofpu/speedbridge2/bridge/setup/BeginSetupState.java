package com.github.tofpu.speedbridge2.bridge.setup;

import com.github.tofpu.speedbridge2.bridge.core.Game;
import com.github.tofpu.speedbridge2.bridge.core.GameHandler;
import com.github.tofpu.speedbridge2.bridge.core.state.StartGameState;

class BeginSetupState extends StartGameState<BridgeSetupHandler, IslandSetup> {
    @Override
    public void apply(BridgeSetupHandler handler, IslandSetup game) {
        SetupPlayer gamePlayer = (SetupPlayer) game.gamePlayer();
        gamePlayer.player().teleport(game.land().getIslandLocation());
    }
}