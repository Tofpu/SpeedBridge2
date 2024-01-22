package com.github.tofpu.speedbridge2.common.bridge.game.state.basic;

import com.github.tofpu.speedbridge2.common.bridge.BridgeGameAPI;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGameData;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.common.bridge.game.event.IslandGameStopEvent;
import com.github.tofpu.speedbridge2.common.bridge.game.state.generic.BridgeStopState;
import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.GameStateType;

public class GameStopState extends BridgeStopState {
    @Override
    public void onGameStateChange(Game<IslandGameData> game, GameStateType<IslandGameData> stateChange) {
        IslandGameData gameData = game.data();
        IslandGamePlayer gamePlayer = gameData.gamePlayer();
        BridgeGameAPI.instance().dispatchEvent(new IslandGameStopEvent((IslandGame) game, gamePlayer));
    }
}
