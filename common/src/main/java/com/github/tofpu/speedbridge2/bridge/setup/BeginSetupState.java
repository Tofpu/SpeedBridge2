package com.github.tofpu.speedbridge2.bridge.setup;

import com.github.tofpu.speedbridge2.bridge.core.state.StartGameState;
import com.github.tofpu.speedbridge2.bridge.core.Game;

class BeginSetupState extends StartGameState<IslandSetupData> {
    @Override
    public void apply(Game<IslandSetupData> game) {
        IslandSetupData data = game.data();
        SetupPlayer gamePlayer = data.player();
        gamePlayer.player().teleport(data.land().getIslandLocation());
    }
}