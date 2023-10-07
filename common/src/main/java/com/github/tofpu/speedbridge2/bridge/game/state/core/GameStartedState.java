package com.github.tofpu.speedbridge2.bridge.game.state.core;

import com.github.tofpu.speedbridge2.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.bridge.game.IslandGameHandler;
import com.github.tofpu.speedbridge2.bridge.game.state.custom.IslandResetGameState;
import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeStartedState;

class GameStartedState extends BridgeStartedState {
    @Override
    public void apply(IslandGameHandler handler, IslandGame game) {
        apply(game);
    }

    public void apply(IslandGame game) {
        if (game.gameState() instanceof IslandResetGameState) {
            return;
        }
        game.player().getPlayer().sendMessage("Game has begun, good luck!");
    }
}
