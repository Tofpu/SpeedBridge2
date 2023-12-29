package com.github.tofpu.speedbridge2.bridge.setup.state;

import com.github.tofpu.speedbridge2.bridge.setup.IslandSetupData;
import com.github.tofpu.speedbridge2.bridge.setup.SetupPlayer;
import com.github.tofpu.speedbridge2.game.state.StartGameState;
import com.github.tofpu.speedbridge2.game.Game;

class StartSetupState extends StartGameState<IslandSetupData> {
    @Override
    public void apply(Game<IslandSetupData> game) {
        IslandSetupData data = game.data();
        SetupPlayer gamePlayer = data.player();
        gamePlayer.player().teleport(data.land().getIslandLocation());
    }
}