package com.github.tofpu.speedbridge2.common.game.state.core;

import com.github.tofpu.speedbridge2.common.game.IslandGame;
import com.github.tofpu.speedbridge2.common.game.IslandGameData;
import com.github.tofpu.speedbridge2.common.game.event.IslandGamePrepareEvent;
import com.github.tofpu.speedbridge2.common.game.state.BridgeGameState;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.GameStateType;

public class GamePrepareState implements BridgeGameState {
    private final EventDispatcherService eventDispatcher;

    public GamePrepareState(EventDispatcherService eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void onGameStateChange(Game<IslandGameData> game, GameStateType<IslandGameData> stateChange) {
        IslandGame islandGame = (IslandGame) game;
        eventDispatcher.dispatchIfApplicable(new IslandGamePrepareEvent(islandGame, game.data().gamePlayer()));
    }
}
