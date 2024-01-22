package com.github.tofpu.speedbridge2.common.bridge.game.state.game;

import com.github.tofpu.speedbridge2.common.bridge.BridgeGameAPI;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGameData;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.common.bridge.game.event.IslandGameResetEvent;
import com.github.tofpu.speedbridge2.common.bridge.game.state.generic.BridgeGameState;
import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.GameStateType;

public class IslandResetGameState implements BridgeGameState {
    @Override
    public void onGameStateChange(Game<IslandGameData> game, GameStateType<IslandGameData> stateChange) {
        IslandGamePlayer player = game.data().gamePlayer();
        IslandGameResetEvent event = new IslandGameResetEvent((IslandGame) game, player);
        BridgeGameAPI.instance().dispatchEvent(event);
    }
}
