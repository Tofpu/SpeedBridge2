package com.github.tofpu.speedbridge2.common.game.state.core;

import com.github.tofpu.speedbridge2.common.game.IslandGame;
import com.github.tofpu.speedbridge2.common.game.IslandGameData;
import com.github.tofpu.speedbridge2.common.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.common.game.event.IslandGameStopEvent;
import com.github.tofpu.speedbridge2.common.game.state.BridgeGameState;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.GameStateType;

public class GameStopState implements BridgeGameState {
    private final EventDispatcherService eventDispatcher;

    public GameStopState(EventDispatcherService eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void onGameStateChange(Game<IslandGameData> game, GameStateType<IslandGameData> stateChange) {
        IslandGameData gameData = game.data();
        IslandGamePlayer gamePlayer = gameData.gamePlayer();
        eventDispatcher.dispatchIfApplicable(new IslandGameStopEvent((IslandGame) game, gamePlayer));
    }
}
