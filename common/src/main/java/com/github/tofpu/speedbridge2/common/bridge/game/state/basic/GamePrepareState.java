package com.github.tofpu.speedbridge2.common.bridge.game.state.basic;

import com.github.tofpu.speedbridge2.common.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGameData;
import com.github.tofpu.speedbridge2.common.bridge.game.event.IslandGamePrepareEvent;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.GameStateType;
import io.github.tofpu.speedbridge.gameengine.StateChangeListener;

public class GamePrepareState implements StateChangeListener<IslandGameData> {
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
