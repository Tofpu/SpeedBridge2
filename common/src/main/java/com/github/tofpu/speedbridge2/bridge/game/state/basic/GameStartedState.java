package com.github.tofpu.speedbridge2.bridge.game.state.basic;

import com.github.tofpu.speedbridge2.bridge.game.IslandGameData;
import com.github.tofpu.speedbridge2.bridge.game.state.BridgeGameStateTag;
import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeStartedState;
import com.github.tofpu.speedbridge2.game.Game;

class GameStartedState extends BridgeStartedState {
    @Override
    public void apply(Game<IslandGameData> game) {
        if (game.state().tag() == BridgeGameStateTag.RESET) {
            return;
        }
        game.data().gamePlayer().getPlayer().sendMessage("Game has begun, good luck!");
    }
}
