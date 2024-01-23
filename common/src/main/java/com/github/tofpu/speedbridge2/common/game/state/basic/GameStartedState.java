package com.github.tofpu.speedbridge2.common.game.state.basic;

import com.github.tofpu.speedbridge2.common.game.BridgeStateTypes;
import com.github.tofpu.speedbridge2.common.game.IslandGameData;
import com.github.tofpu.speedbridge2.common.game.state.generic.BridgeStartedState;
import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.GameStateType;

public class GameStartedState extends BridgeStartedState {
    @Override
    public void onGameStateChange(Game<IslandGameData> game, GameStateType<IslandGameData> stateChange) {
        if (stateChange == BridgeStateTypes.RESET) {
            return;
        }
        game.data().gamePlayer().getPlayer().sendMessage("Game has begun, good luck!");
    }
}
