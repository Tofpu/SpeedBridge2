package com.github.tofpu.speedbridge2.common.setup.state;

import com.github.tofpu.speedbridge2.common.gameextra.state.StartGameState;
import com.github.tofpu.speedbridge2.common.setup.IslandSetupData;
import com.github.tofpu.speedbridge2.common.setup.IslandSetupPlayer;
import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.GameStateType;

public class StartSetupState extends StartGameState<IslandSetupData> {
    @Override
    public void onGameStateChange(Game<IslandSetupData> game, GameStateType<IslandSetupData> prevState, GameStateType<IslandSetupData> newState) {
        IslandSetupData data = game.data();
        IslandSetupPlayer gamePlayer = data.player();
        gamePlayer.player().teleport(data.land().getIslandLocation());
    }
}