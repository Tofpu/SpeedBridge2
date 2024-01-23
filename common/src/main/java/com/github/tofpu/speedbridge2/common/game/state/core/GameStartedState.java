package com.github.tofpu.speedbridge2.common.game.state.core;

import com.github.tofpu.speedbridge2.common.game.IslandGameData;
import com.github.tofpu.speedbridge2.common.game.IslandGameStates;
import com.github.tofpu.speedbridge2.common.game.state.BridgeGameState;
import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.GameStateType;

public class GameStartedState implements BridgeGameState {
    @Override
    public void onGameStateChange(Game<IslandGameData> game, GameStateType<IslandGameData> stateChange) {
        if (stateChange == IslandGameStates.RESET) {
            return;
        }
        game.data().gamePlayer().getPlayer().sendMessage("Game has begun, good luck!");
    }
}
