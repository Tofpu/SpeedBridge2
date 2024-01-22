package com.github.tofpu.speedbridge2.common.bridge.setup.state;

import com.github.tofpu.speedbridge2.common.bridge.setup.IslandSetupData;
import com.github.tofpu.speedbridge2.common.bridge.setup.SetupPlayer;
import com.github.tofpu.speedbridge2.common.gameextra.state.StartGameStateType;
import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.GameStateType;

public class StartSetupState extends StartGameStateType<IslandSetupData> {
    @Override
    public void onGameStateChange(Game<IslandSetupData> game, GameStateType<IslandSetupData> stateChange) {
        IslandSetupData data = game.data();
        SetupPlayer gamePlayer = data.player();
        gamePlayer.player().teleport(data.land().getIslandLocation());
    }
}