package com.github.tofpu.speedbridge2.common.bridge.game.state.basic;

import com.github.tofpu.speedbridge2.common.bridge.BridgeGameAPI;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGameData;
import com.github.tofpu.speedbridge2.common.bridge.game.event.IslandGamePrepareEvent;
import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.GameStateType;
import io.github.tofpu.speedbridge.gameengine.StateChangeListener;

public class GamePrepareState implements StateChangeListener<IslandGameData> {
    @Override
    public void onGameStateChange(Game<IslandGameData> game, GameStateType<IslandGameData> stateChange) {
        IslandGame islandGame = (IslandGame) game;
        BridgeGameAPI.instance().dispatchEvent(new IslandGamePrepareEvent(islandGame, game.data().gamePlayer()));
    }
}
